/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import com.github.KeyMove.EventsManager.EventManger;
import com.github.KeyMove.EventsManager.Tools.Ref;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import java.io.File;
import lib.org.luaj.vm2.lib.jse.JsePlatform;
import org.bukkit.Bukkit;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 *
 * @author Administrator
 */
@Mod(modid="InGameLuaEdit", name="InGameLuaEdit", version="1.0.0")
public class InGameLuaEditForge {
    Globals LuaVM;
    LuaVMTools LuaTools;
    
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
    
    public void InitLuaVM(File luapath){
        LuaVM=JsePlatform.standardGlobals();
        LuaC.install(LuaVM);
        LuaVM.load("print(\"[InGameLuaEdit]load OK!\")").call();
        LuaVM.compiler=LuaC.instance;
        LuaVM.set("Ref", CoerceJavaToLua.coerce(Ref.class));
        LoadLua(luapath);
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        File f=new File(event.getModConfigurationDirectory(),"InGameLuaEdit");
        if(!f.exists())
            f.mkdir();
        InitLuaVM(f);
        System.out.println("[InGameLuaEdit] 加载成功!");
    }
}
