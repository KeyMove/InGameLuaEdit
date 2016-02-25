/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove.EventsManager;

import com.github.KeyMove.EventsManager.Tools.类解析器;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static java.lang.System.out;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Administrator
 */
public class EventsBuild {
    
    static List<String> UseEventsList=new ArrayList<>();
    static List<String> PacketInList=new ArrayList<>();
    static Class PacketHookClass=PacketHookBase.class;
    static boolean isPacketLoad=false;
    static String PacketInListenerName;
    static boolean log=true;
    public static Class LoadClassFile(String filepath,ClassLoader cloader){
        Class desClass=null;
        String classname=filepath.substring(filepath.lastIndexOf("\\")+1, filepath.lastIndexOf("."));
        String p=filepath.substring(0,filepath.lastIndexOf("\\"));
        
        URL[] path;
        try {
            File file=new File(p);
            path = new URL[]{file.toURI().toURL()};
            URLClassLoader loader=new URLClassLoader(path,cloader);
            desClass=loader.loadClass(classname);
        } catch (MalformedURLException | ClassNotFoundException ex) {
            Log(ex);
        }
        return desClass;
    }
    
    static List<String> getPacketClass(String pahtname,List<String> classname){
        String path=Bukkit.class.getResource(pahtname).getFile().replaceAll("%20", " ");
        String[] jarInfo = path.split("!");  
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));  
        String packagePath = jarInfo[1].substring(1);  
        List<String> l=new ArrayList<>();
        File f=new File(jarFilePath+"!/"+packagePath);
        try {  
            JarFile jarFile = new JarFile(jarFilePath);  
            Enumeration<JarEntry> entrys = jarFile.entries();  
            while (entrys.hasMoreElements()) {  
                JarEntry jarEntry = entrys.nextElement();  
                String entryName = jarEntry.getName();  
                if (entryName.contains(packagePath)&&entryName.endsWith(".class")) { 
                    entryName = entryName.substring(0, entryName.lastIndexOf("."));
                    if(entryName.endsWith("Event"))
                    {
                        l.add(entryName);
                    }  
                }
            }  
        } catch (IOException e) {  
            Log(e);
        }  
        return l;  
    }
    static List<String> getPacketAllClass(String path){
        return getPacketClass(path, null);
    }
    
    public static List<String> getPacketInClass(){
        String ver=Bukkit.getServer().getClass().getPackage().getName();
        ver=ver.substring(ver.lastIndexOf('.')+1);
        String path=Bukkit.class.getResource("").getFile().replaceAll("%20", " ");
        path=path.substring(0, path.lastIndexOf("org"));
        path+="net/minecraft/server/"+ver;
        String[] jarInfo = path.split("!");  
        String jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"));
        String packagePath = jarInfo[1].substring(1);  
        List<String> l=new ArrayList<>();
        File f=new File(jarFilePath+"!/"+packagePath);
        try {  
            JarFile jarFile = new JarFile(jarFilePath);  
            Enumeration<JarEntry> entrys = jarFile.entries();  
            while (entrys.hasMoreElements()) {  
                JarEntry jarEntry = entrys.nextElement();  
                String entryName = jarEntry.getName();  
                if (entryName.contains(packagePath)&&entryName.endsWith(".class")) { 
                    entryName = entryName.substring(0, entryName.lastIndexOf("."));
                    if(entryName.contains("Packet")&&entryName.contains("PlayIn")&&!entryName.contains("$"))
                    {
                        l.add(entryName);
                    }  
                }
            }  
        } catch (IOException e) {  
            Log(e);
        }  
        return l;  
    }
    
    static void AddAllPacketEvent(File f){
        类解析器 builder=new 类解析器();
        builder.解析文件(f);
        List<String> utf8list=builder.获取所有UTF8常量();
        String ver=Bukkit.getServer().getClass().getPackage().getName();
        ver=ver.substring(ver.lastIndexOf('.')+1);
        String path=null;
        for(String value:utf8list){
            String newpath;
            if(value.contains("net/minecraft/server/"))
            {
                if(value.contains("Listener")&&!value.contains(";"))
                {
                        builder.替换指定UTF8常量(value,PacketInListenerName);
                        Log(value);
                        Log(PacketInListenerName);
                        continue;
                }
                if(path!=null){
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                }
                else{
                    path=value.substring(value.indexOf("net/minecraft/server/")+21, value.lastIndexOf('/'));
                    Log("net:"+path);
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                    Log(value);
                    Log(newpath);
                }
            }else if(value.contains("org/bukkit/craftbukkit/")){
                if(path!=null){
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                }
                else{
                    path=value.substring(value.indexOf("org/bukkit/craftbukkit/")+23, value.lastIndexOf('/'));
                    Log("org:"+path);
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                    Log(value);
                    Log(newpath);
                }
            }
        }
        builder.保存文件(f);
    }
    
    static void AddAllPacketEvent(InputStream f,OutputStream o){
        类解析器 builder=new 类解析器();
        builder.解析(f);
        List<String> utf8list=builder.获取所有UTF8常量();
        String ver=Bukkit.getServer().getClass().getPackage().getName();
        ver=ver.substring(ver.lastIndexOf('.')+1);
        String path=null;
        for(String value:utf8list){
            String newpath;
            if(value.contains("net/minecraft/server/"))
            {
                if(value.contains("Listener")&&!value.contains(";"))
                {
                        builder.替换指定UTF8常量(value,PacketInListenerName);
                        Log(value);
                        Log(PacketInListenerName);
                        continue;
                }
                if(path!=null){
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                }
                else{
                    path=value.substring(value.indexOf("net/minecraft/server/")+21, value.lastIndexOf('/'));
                    Log("net:"+path);
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                    Log(value);
                    Log(newpath);
                }
            }else if(value.contains("org/bukkit/craftbukkit/")){
                if(path!=null){
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                }
                else{
                    path=value.substring(value.indexOf("org/bukkit/craftbukkit/")+23, value.lastIndexOf('/'));
                    Log("org:"+path);
                    newpath=value.replace(path, ver);
                    builder.替换指定UTF8常量(value, newpath);
                    Log(value);
                    Log(newpath);
                }
            }
        }
        builder.保存(o);
    }
    
    static void AddAllEvent(File f,List<String> str){
        Log("开始查找事件");
        List<String> AllEvents=getPacketAllClass("event");
        类解析器 builder=new 类解析器();
        builder.解析文件(f);
        类解析器.方法 method=builder.获取方法("Z");
        类解析器.代码属性 code;
        List<Object> attList;
        类解析器.局部变量属性 tempvar;
        int count=0;
        Log("开始加载事件");
        for(String name:AllEvents){
            String value=name.substring(name.lastIndexOf('/')+1);
            if(str.indexOf(value)!=-1){
                Log("加载"+value);
                code=(类解析器.代码属性) method.寻找属性(类解析器.静态属性列表.Code);
                attList=code.寻找属性(类解析器.静态属性列表.LocalVariableTable);
                tempvar=(类解析器.局部变量属性) attList.get(attList.size()-1);
                method.设置方法名称("A"+count);
                method.设置方法返回值与参数(null,name);
                code.字节码[2]=(byte)(count/256);
                code.字节码[3]=(byte)(count%256);
                tempvar.设置局部变量属性("e", name);
                attList.set(attList.size()-1, tempvar);
                code.设置属性(类解析器.静态属性列表.LocalVariableTable, attList);
                method.设置属性(类解析器.静态属性列表.Code, code);
                if(count!=0)
                    builder.添加方法(method);
                count++;
                method=method.克隆();
            }
        }
        builder.保存文件(f);
    }
    
    static void AddAllEvent(InputStream f,OutputStream fo,List<String> str){
        Log("开始查找事件");
        List<String> AllEvents=getPacketAllClass("event");
        类解析器 builder=new 类解析器();
        builder.解析(f);
        类解析器.方法 method=builder.获取方法("Z");
        类解析器.代码属性 code;
        List<Object> attList;
        类解析器.局部变量属性 tempvar;
        int count=0;
        Log("开始加载事件");
        for(String name:AllEvents){
            String value=name.substring(name.lastIndexOf('/')+1);
            if(str.indexOf(value)!=-1){
                Log("加载"+value);
                code=(类解析器.代码属性) method.寻找属性(类解析器.静态属性列表.Code);
                attList=code.寻找属性(类解析器.静态属性列表.LocalVariableTable);
                tempvar=(类解析器.局部变量属性) attList.get(attList.size()-1);
                method.设置方法名称("A"+count);
                method.设置方法返回值与参数(null,name);
                code.字节码[2]=(byte)(count/256);
                code.字节码[3]=(byte)(count%256);
                tempvar.设置局部变量属性("e", name);
                attList.set(attList.size()-1, tempvar);
                code.设置属性(类解析器.静态属性列表.LocalVariableTable, attList);
                method.设置属性(类解析器.静态属性列表.Code, code);
                if(count!=0)
                    builder.添加方法(method);
                count++;
                method=method.克隆();
            }
        }
        builder.保存(fo);
    }
    
    public static void SaveFile(Plugin plugin,String FileName,String SavePath){
         try {
                InputStream ip=plugin.getResource(FileName);
                FileOutputStream op=new FileOutputStream(new File(plugin.getDataFolder(),SavePath));
                byte[] buff=new byte[1024];
                int len;
                while((len=ip.read(buff))!=-1){
                    op.write(buff, 0, len);
                }
                ip.close();
                op.close();
        } catch (IOException ex) {
            Logger.getLogger(EventsBuild.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void SaveFile(String FileName,File path){
         try {
             FileOutputStream op;
             try (InputStream ip = EventsBuild.class.getResourceAsStream(FileName)) {
                 op = new FileOutputStream(path);
                 byte[] buff=new byte[1024];
                 int len;
                 while((len=ip.read(buff))!=-1){
                     op.write(buff, 0, len);
                 }
             }
                op.close();
        } catch (IOException ex) {
            Logger.getLogger(EventsBuild.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void BuildPacketInFile(Plugin plugin){
        File f=new File(plugin.getDataFolder(),"Ext\\");
        if(!f.exists()){
            f.mkdirs();
        }
        f=new File(f,"PacketHook.class");
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(EventsBuild.class.getName()).log(Level.SEVERE, null, ex);
            }
            SaveFile(plugin, "PacketData.dat", "Ext\\PacketHook.class");
        }
        AddAllPacketEvent(f);
    }
    
    public static void BuildEventsFile(Plugin plugin,File f){
        String path=f.getPath();
        File dir=new File(path.substring(0, path.lastIndexOf('\\')));
        if(!dir.exists())
            dir.mkdirs();
        if(!f.exists())
        {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(EventsBuild.class.getName()).log(Level.SEVERE, null, ex);
            }
            SaveFile(plugin,"ClassData.dat","Ext\\Events.class");
        }
        File configfile=new File(plugin.getDataFolder(),"Ext\\config.yml");
        if(!configfile.exists())
            SaveFile(plugin, "config.yml", "Ext\\config.yml");
        YamlConfiguration config=YamlConfiguration.loadConfiguration(configfile);
        UseEventsList=config.getStringList("UseEventsList");
        Log("事件列表长度"+UseEventsList.size());
        AddAllEvent(f, UseEventsList);
    }
    
    public static void LoadPacketEvents(Plugin plugin){
        PacketInList=getPacketInClass();
        for(String v:PacketInList)
            if(v.contains("Listener"))
            {
                isPacketLoad=true;
                PacketInListenerName=v;
                Log(v);
                break;
            }
        if(!isPacketLoad){
            Log("未能注册包");
            return;
        }
        InputStream ip=plugin.getResource("PacketData.dat");
        ByteArrayOutputStream bo=new ByteArrayOutputStream();
        AddAllPacketEvent(ip, bo);
        try {
            bo.close();
            ip.close();
            PacketHookClass=类解析器.动态加载类(plugin.getClass().getClassLoader(), bo.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(EventsBuild.class.getName()).log(Level.SEVERE, null, ex);
        }
        Log(PacketHookClass);
    }
    
    public static EventManger LoadEvents(Plugin plugin){
        File configfile=new File(plugin.getDataFolder(),"config.yml");
        if(!configfile.exists())
            SaveFile(plugin, "config.yml", "config.yml");
        YamlConfiguration config=YamlConfiguration.loadConfiguration(configfile);
        UseEventsList=config.getStringList("UseEventsList");
        log=config.getBoolean("Log");
        if(config.getBoolean("PacketEvent")){
            LoadPacketEvents(plugin);
        }
        InputStream ip=plugin.getResource("ClassData.dat");
        ByteArrayOutputStream bo=new ByteArrayOutputStream();
        Log("事件列表长度"+UseEventsList.size());
        AddAllEvent(ip,bo, UseEventsList);
        try {
            bo.close();
            ip.close();
            EventManger obj=(EventManger)类解析器.动态加载类(plugin.getClass().getClassLoader(), bo.toByteArray()).newInstance();
            return obj;
        } catch (InstantiationException | IllegalAccessException | IOException ex) {
            Logger.getLogger(EventsBuild.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static PacketHookBase newPacketHook(){
        if(PacketHookClass!=null){
            try {
                return (PacketHookBase) PacketHookClass.newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(EventsBuild.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
    
    public static void GetPacketIn(Plugin plugin){
        File f=new File(plugin.getDataFolder(),"Ext\\PacketHook.class");
        PacketInList=getPacketInClass();
        for(String v:PacketInList)
            if(v.contains("Listener"))
            {
                isPacketLoad=true;
                PacketInListenerName=v;
                Log(v);
                break;
            }
        if(!isPacketLoad){
            Log("未能注册包");
            return;
        }
        if(!f.exists()){
            BuildPacketInFile(plugin);
        }
        f=new File(plugin.getDataFolder(),"\\Ext\\PacketHook.class");
        PacketHookClass=LoadClassFile(f.getPath(),plugin.getClass().getClassLoader());
        Log(PacketHookClass);
    }
    
    public static EventManger GetEvents(Plugin plugin){
        EventManger EventsObject=null;
        File classfile=new File(plugin.getDataFolder(),"\\Ext\\Events.class");
        if(!classfile.exists()){
            BuildEventsFile(plugin, classfile);
            GetPacketIn(plugin);
            //plugin.getClass().getClassLoader()
        }
            Class EventClass=LoadClassFile(classfile.getPath(),plugin.getClass().getClassLoader());        
            if(EventClass!=null){
                try {
                    EventsObject=(EventManger)EventClass.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    Log(ex);
                }
            }
        return EventsObject;
    }
    public static void Log(Object obj){
        if(log)
            out.print("[InGameLuaEdit] "+obj);
    }
    
}
