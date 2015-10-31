/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import com.github.KeyMove.EventsManager.EventsBuild;
import com.github.KeyMove.EventsManager.Tools.Ref;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import lib.org.luaj.vm2.lib.jse.JsePlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.ServerChatEvent;
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
    LuaValue KeyEvent=null;
    static File luaDir;
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
        LuaVM=JsePlatform.standardGlobals();
        LuaC.install(LuaVM);
        LuaVM.load("print(\"[InGameLuaEdit]load OK!\")").call();
        LuaVM.compiler=LuaC.instance;
        LuaVM.set("LuaKey", CoerceJavaToLua.coerce(Key));
        LuaVM.set("Ref", CoerceJavaToLua.coerce(Ref.class));
    }
    
    @SideOnly(Side.CLIENT)
    public static KeyBinding Key=new KeyBinding("LuaKey", 48, "InGameLuaEdit");
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        FMLCommonHandler.instance().bus().register(this);
        ClientRegistry.registerKeyBinding(Key);
        File f=new File(event.getModConfigurationDirectory(),"InGameLuaEdit");
        luaDir=new File(event.getModConfigurationDirectory(),"InGameLuaEdit");
        if(!f.exists())
            f.mkdir();
        f=new File(f,"/main.lua");
        if(!f.exists())
        {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(InGameLuaEditForge.class.getName()).log(Level.SEVERE, null, ex);
            }
            EventsBuild.SaveFile("/main.lua", f);
        }
        try{
            InitLuaVM();
            LoadLua(luaDir);
        }catch(Exception e){
            System.out.println(e);
        }        
        System.out.println("[InGameLuaEdit] 加载成功!");
    }
    public void ChatEvent(ServerChatEvent event){
        
    }
    
    
    @SubscribeEvent
    public void KeyEvent(InputEvent.KeyInputEvent event){
        if(KeyEvent!=null){
            //System.out.println("Call LuaKeyPress");
            try{
            if(!KeyEvent.call().isnil())
            {
                InitLuaVM();
                LoadLua(luaDir);
                KeyEvent=LuaVM.get("LuaKeyPress");
            }
            }catch(Exception e){
                System.out.println(e);
            }
            
        }
        else{
            try{
            System.out.println(KeyEvent);
            LoadLua(luaDir);
            KeyEvent=LuaVM.get("LuaKeyPress");
            }
            catch(Exception e){
                System.out.println(e);
            }
        }
        //System.out.println(event);
        //InitLuaVM();
        //LoadLua(luaDir);
        //Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("已重新加载lua文件!"));
    }
}
