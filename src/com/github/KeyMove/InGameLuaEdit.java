/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import com.github.KeyMove.EventsManager.EventManger;
import static com.github.KeyMove.EventsManager.EventsBuild.GetEvents;
import java.io.File;
import static java.lang.System.out;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.org.luaj.vm2.lib.jse.JsePlatform;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;

/**
 *
 * @author Administrator
 */
public class InGameLuaEdit extends JavaPlugin{
    Globals LuaVM;
    EventManger eventManger;
    
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
        LuaVM.set("Tools", new LuaVMTools(this,eventManger));
        LuaVM.set("OBCPATH",LuaValue.valueOf(obc));
        LuaVM.set("NMSPATH",LuaValue.valueOf(nms));
    }
    
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
    
    @Override
    public void onEnable() {
        eventManger=GetEvents(this);
        eventManger.Setup(this);
        out.print(eventManger);
        InitLuaVM();
        getServer().getPluginManager().registerEvents(eventManger, this);
        LoadLuaFile();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            new PacketHook((Player)sender);
        }
        else{
            if(args.length!=0){
                Player p=getServer().getPlayer(args[0]);
                if(p!=null)
                    new PacketHook(p);
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
