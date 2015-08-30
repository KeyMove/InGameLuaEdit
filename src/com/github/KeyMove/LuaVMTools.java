/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import com.github.KeyMove.EventsManager.EventManger;
import com.github.KeyMove.EventsManager.EventsBuild;
import com.github.KeyMove.EventsManager.PacketHookBase;
import static java.lang.System.out;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import static org.luaj.vm2.LuaValue.NIL;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.VarArgFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

/**
 *
 * @author Administrator
 */
public class LuaVMTools extends LuaTable{
    
    static SimpleCommandMap g_CommandMap;
    static Map g_knownCommand;
    static List<String> g_CommandName=new ArrayList<>();
    
    static Plugin g_Plugin;
    
    static List<LuaBukkitThread> g_ThreadList=new ArrayList<>();
    static List<Integer> g_RunnableList=new ArrayList<>();
    
    public static Map<String,List<LuaFunction>> EventMap=new HashMap<>();
    
    static Map<Player, PacketHookBase> g_PacketHook=new HashMap<>();
    
    class LuaBukkitCommand extends Command {
        LuaFunction LuaMethod;
        public LuaBukkitCommand(String name, String description, String usageMessage, List<String> aliases, LuaFunction LuaMethod){
            super(name,description,usageMessage,aliases);
            this.LuaMethod=LuaMethod;
        }
        public LuaBukkitCommand(String name, LuaFunction LuaMethod){
            super(name);
            this.LuaMethod=LuaMethod;
        }
        @Override
        public boolean execute(CommandSender cs, String string, String[] strings) {
            this.LuaMethod.call(CoerceJavaToLua.coerce(cs));
            return true;
        }
    }
    
    class LuaBukkitThread extends Thread{
        LuaFunction LuaMethod;
        public LuaBukkitThread(LuaFunction LuaMethod) {
            this.LuaMethod = LuaMethod;
        }
        @Override
        public void run() {
            this.LuaMethod.call(CoerceJavaToLua.coerce(this));
        }
    }
    
    class LuaBukkitRunnable implements Runnable{
        LuaFunction LuaMethod;

        public LuaBukkitRunnable(LuaFunction LuaMethod) {
            this.LuaMethod = LuaMethod;
        }
        public void Start(){
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(g_Plugin, this, 1);
        }
        public int Start(int time){
            return Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(g_Plugin, this, 1, time);
        }
        @Override
        public void run() {
            this.LuaMethod.call();
        }
        
    }
    
    static void unregisterCommand(String name){
        if(g_knownCommand.containsKey(name))
            g_knownCommand.remove(name);
    }
    
    public static void ReInit(){
        EventMap.clear();
        for(String name:g_CommandName){
            unregisterCommand(name);
        }
        g_CommandName.clear();
        for(LuaBukkitThread t:g_ThreadList){
            t.stop();
        }
        g_ThreadList.clear();
        for(int id:g_RunnableList){
            g_Plugin.getServer().getScheduler().cancelTask(id);
        }
        g_RunnableList.clear();
    }
    
    public LuaVMTools(Plugin plugin,EventManger eventManger) {
        g_Plugin=plugin;
        String ver=Bukkit.getServer().getClass().getPackage().getName();
        ver=ver.substring(ver.lastIndexOf('.')+1)+".";
        String nms="net.minecraft.server."+ver;
        try {
            Field fd=plugin.getServer().getPluginManager().getClass().getDeclaredField("commandMap");
            fd.setAccessible(true);
            g_CommandMap=(SimpleCommandMap)fd.get(plugin.getServer().getPluginManager());
            Field fd2=g_CommandMap.getClass().getDeclaredField("knownCommands");
            fd2.setAccessible(true);
            g_knownCommand=(Map) fd2.get(g_CommandMap);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ex) {
            Logger.getLogger(LuaVMTools.class.getName()).log(Level.SEVERE, null, ex);
        }
            set("Command", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args){
                    if(args.isfunction(2)){
                        g_CommandMap.register("LuaBukkit", new LuaBukkitCommand(args.tojstring(1), args.checkfunction(2)));
                        g_CommandName.add(args.tojstring(1));
                    }
                    else if(args.isfunction(4))
                    {
                        g_CommandMap.register("LuaBukkit", new LuaBukkitCommand(args.tojstring(1), args.tojstring(2), args.tojstring(3), new ArrayList(), args.checkfunction(4)));
                    }
                    return NIL;
                }
            });
            set("AsyncThread", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    LuaBukkitThread thread=new LuaBukkitThread(args.checkclosure(1));
                    g_ThreadList.add(thread);
                    thread.start();
                    return NIL;
                }
            });
            set("SyncThread", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    if(!args.isfunction(1)){
                        out.print("同步线程参数错误");
                        return NIL;
                    }
                    LuaBukkitRunnable thread=new LuaBukkitRunnable(args.checkclosure(1));
                    if(args.isnumber(2)){
                        int id=thread.Start(args.toint(2));
                        g_RunnableList.add(id);
                        return LuaValue.valueOf(id);
                    }else{
                        thread.Start();
                    }
                    return NIL;
                }
            });
            set("CancelSync", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    if(args.isnumber(1))
                        Bukkit.getServer().getScheduler().cancelTask(args.checkint(1));
                    else
                        out.print("取消同步线程参数错误");
                    return NIL;
                }
            });
            set("Event", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    if(args.isstring(1)&&args.isfunction(2)){
                        eventManger.RegisterEvent(args.tojstring(1), args.checkfunction(2));
                    }
                    else{
                        out.print("注册事件失败");
                    }
                    return NIL;
                }
            });
            set("PacketIn", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    if(args.isfunction(2)){
                        Player p;
                        if(args.isstring(1)){
                            p=g_Plugin.getServer().getPlayer(args.tojstring(1));
                        }
                        else{
                            p=(Player) CoerceLuaToJava.coerce(args.arg(1), Player.class);
                        }
                        if(p!=null)
                        {
                            if(g_PacketHook.containsKey(p))
                            {
                                g_PacketHook.get(p).AddFunction(args.checkfunction(2));
                            }
                            else{
                                PacketHookBase b=EventsBuild.newPacketHook();
                                g_PacketHook.put(p, b);
                                b.HookPlayer(p);
                                b.AddFunction(args.checkfunction(2));
                            }
                        }
                    }
                    else{
                        out.print("注册事件失败");
                    }
                    return NIL;
                }
            });
            set("LoadAPI", new VarArgFunction() {
                @Override
                public Varargs invoke(Varargs args) {
                    if(args.isstring(1)){
                        String pname=args.tojstring(1);
                        Plugin p=Bukkit.getServer().getPluginManager().getPlugin(pname);
                        if(p!=null&&p.isEnabled()){
                            if(args.isstring(2)){
                                String cname=args.tojstring(2);
                                try {
                                    Class fc=Class.forName(cname);
                                    out.print(fc);
                                    RegisteredServiceProvider rp=Bukkit.getServicesManager().getRegistration(fc);
                                    out.print(rp);
                                    if(rp!=null)
                                        return CoerceJavaToLua.coerce(rp.getProvider());
                                    else
                                        return NIL;
                                } catch (ClassNotFoundException ex) {
                                   out.print(ex);
                                }
                            }
                            else
                                return CoerceJavaToLua.coerce(p);
                        }
                    }
                    return NIL;
                }
            });
        }
}
