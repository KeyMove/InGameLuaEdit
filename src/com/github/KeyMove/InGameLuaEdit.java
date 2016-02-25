/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import com.github.KeyMove.EventsManager.EventManger;
import com.github.KeyMove.EventsManager.EventsBuild;
import static com.github.KeyMove.EventsManager.EventsBuild.GetEvents;
import static com.github.KeyMove.EventsManager.EventsBuild.LoadEvents;
import com.github.KeyMove.EventsManager.Tools.Ref;
import java.io.File;
import static java.lang.System.out;
import lib.org.luaj.vm2.lib.jse.JsePlatform;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 *
 * @author Administrator
 */
public class InGameLuaEdit extends JavaPlugin{
    Globals LuaVM;
    LuaVMTools LuaTools;
    EventManger eventManger;
    
    void LoadLuaFile(){
        LoadLua(new File(getDataFolder(),"Lua"));
    }
    
    void LoadLua(File f){
        for(File mf:f.listFiles()){
            if(mf.isDirectory()){
                LoadLua(mf);
            }
            else{
                String p=mf.getAbsolutePath();
                if(p.endsWith(".lua"))
                    LuaVM.loadfile(p).call();
            }
        }
    }
    
    public void InitLuaVM(){
        File luadir=new File(getDataFolder(),"lua");
        if(!luadir.exists()){
            luadir.mkdir();
        }
        LuaVM=JsePlatform.standardGlobals();
        LuaC.install(LuaVM);
        LuaVM.load("print(\"[InGameLuaEdit]load OK!\")").call();
        LuaVM.compiler=LuaC.instance;
        String ver=Bukkit.getServer().getClass().getPackage().getName();
        ver=ver.substring(ver.lastIndexOf('.')+1)+".";
        String obc="org.bukkit.craftbukkit."+ver;
        String nms="net.minecraft.server."+ver;
        LuaVM.set("Tools", LuaTools);
        LuaVM.set("Ref", CoerceJavaToLua.coerce(Ref.class));
        LuaVM.set("OBCPATH",LuaValue.valueOf(obc));
        LuaVM.set("NMSPATH",LuaValue.valueOf(nms));
        LoadLuaFile();
    }
    
    
    
    @Override
    public void onEnable() {
        eventManger=LoadEvents(this);
        out.print("Bytecode Load!");
        out.print(eventManger);
        eventManger.Setup(this);
        LuaTools=new LuaVMTools(this, eventManger);
        //out.print(eventManger);
        getServer().getPluginManager().registerEvents(eventManger, this);
        out.print(this.getClass().getTypeName());
        //EventsBuild.GetPacketIn(this);
        InitLuaVM();
        //out.print(Bukkit.class.getResource("event").getFile().replaceAll("%20", " "));
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(eventManger);
        eventManger.ClearEvent();
        LuaVMTools.ReInit();
    }
    
    
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("op")){
            if(args.length!=0)
            {
                switch(args[0]){
                    case "restart":
                        //HandlerList.unregisterAll(eventManger);
                        eventManger.ClearEvent();
                        LuaVMTools.ReInit();
                        InitLuaVM();
                        break;
                    case "rebuild":
                        HandlerList.unregisterAll(eventManger);
                        eventManger.ClearEvent();
                        LuaVMTools.ReInit();
                        eventManger=LoadEvents(this);
                        eventManger.Setup(this);
                        LuaTools=new LuaVMTools(this, eventManger);
                        getServer().getPluginManager().registerEvents(eventManger, this);
                        EventsBuild.GetPacketIn(this);
                        InitLuaVM();
                        out.print("rebuild OK!");
                        break;
                }
            }
            else{
                sender.sendMessage("[InGameLuaEdit] /lua restart - 重新加载lua文件");
                sender.sendMessage("[InGameLuaEdit] /lua rebuild - 重新构造所有配置");
            }
        }
        return true;
    }
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
    }
    
}
