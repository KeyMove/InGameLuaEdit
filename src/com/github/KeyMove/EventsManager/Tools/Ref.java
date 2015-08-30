/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove.EventsManager.Tools;

import static java.lang.System.out;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class Ref {
    static Map<String,Class> g_Classmap=new HashMap<>();
    static Map<Class,Map<String,Method>> g_Methodmap=new HashMap<>();
    static Map<Class,Map<String,Field>> g_Fieldmap=new HashMap<>();
    static Map<Class,Map<Integer,Constructor>> g_ConstructorMap=new HashMap<>();
    
    public static Class getClassName(String name){
        Class t=null;
        if(g_Classmap.containsKey(name)){
            return g_Classmap.get(name);
        }
        try {
            t = Class.forName(name);
            g_Classmap.put(name, t);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
        return t;
    }
    
    public static Object getNewClass(String name){
        try {
            return getClassName(name).newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Object getMembers(String name,Class c,Object o){
        Object oc=null;
        if(!g_Classmap.containsKey(c.getName()))
            c=getClassName(c.getName());
        Map<String,Field> p;
        Field f=null;
        if(!g_Fieldmap.containsKey(c)){
            p=new HashMap<>();
            g_Fieldmap.put(c, p);
        }
        else{
            p=g_Fieldmap.get(c);
        }
        try {
            f=c.getDeclaredField(name);
            f.setAccessible(true);
            p.put(name, f);
            oc=f.get(o);
        } catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
        return oc;
    }
    
    public static Object getMembers(String name,Object o){
        Object oc=null;
        Class c=o.getClass();
        if(!g_Classmap.containsKey(c.getName()))
            c=getClassName(c.getName());
        Map<String,Field> p;
        Field f=null;
        if(!g_Fieldmap.containsKey(c)){
            p=new HashMap<>();
            g_Fieldmap.put(c, p);
        }
        else{
            p=g_Fieldmap.get(c);
        }
        try {
            f=c.getDeclaredField(name);
            f.setAccessible(true);
            p.put(name, f);
            oc=f.get(o);
        } catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
        return oc;
    }
    
    public static void setMembers(String name,Class c,Object o,Object in){
        if(!g_Classmap.containsKey(c.getName()))
            c=getClassName(c.getName());
        Map<String,Field> p;
        Field f=null;
        if(!g_Fieldmap.containsKey(c)){
            p=new HashMap<>();
            g_Fieldmap.put(c, p);
        }
        else{
            p=g_Fieldmap.get(c);
        }
        try {
            f=c.getDeclaredField(name);
            f.setAccessible(true);
            p.put(name, f);
            f.set(o,in);
        } catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void setMembers(String name,Object o,Object in){
        Class c=o.getClass();
        if(!g_Classmap.containsKey(c.getName()))
            c=getClassName(c.getName());
        Map<String,Field> p;
        Field f=null;
        if(!g_Fieldmap.containsKey(c)){
            p=new HashMap<>();
            g_Fieldmap.put(c, p);
        }
        else{
            p=g_Fieldmap.get(c);
        }
        try {
            f=c.getDeclaredField(name);
            f.setAccessible(true);
            p.put(name, f);
            f.set(o,in);
        } catch (NoSuchFieldException | SecurityException |IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static Constructor getConstructor(String name,int id,Object[] type){
        Class c=getClassName(name);
        Map<Integer,Constructor> p=null;
        Class[] list=null;
        if(type.length!=0)
        {
            list=new Class[type.length];
            for(int i=0;i<type.length;i++)
            {
                list[i]=type[i].getClass();
                if(list[i]==Integer.class)
                {
                    list[i]=int.class;
                    continue;
                }
                if(list[i]==Double.class)
                {
                    list[i]=double.class;
                    continue;
                }
                if(list[i]==Boolean.class)
                {
                    list[i]=boolean.class;
                }
                if(list[i]==Class.class)
                    list[i]=(Class) type[i];
            }
        }
        if(g_ConstructorMap.containsKey(c))
        {
            return g_ConstructorMap.get(c).get(id);
        }
        else{
            p=new HashMap<>();
            g_ConstructorMap.put(c, p);
        }
        Constructor ct=null;
        try {
            ct=c.getConstructor(list);
            p.put(id, ct);
            return ct;
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Object NewClass(String name,int id,Object[] type){
        Class c=getClassName(name);
        Constructor ct=getConstructor(name,id, type);
        try {
            //for(Object o:type)
              //  out.print(o+"-"+o.getClass());
            if(ct!=null)
                return ct.newInstance(type);
        } catch (SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static Object Invoke(String name,Object o,Object... arg){
        Class c=o.getClass();
        Class[] tp;
        Object oc=null;
        if(!g_Classmap.containsKey(c.getName()))
            c=getClassName(c.getName());
        Map<String,Method> p;
        Method f=null;
        if(!g_Methodmap.containsKey(c)){
            p=new HashMap<>();
            g_Methodmap.put(c, p);
        }
        else{
            p=g_Methodmap.get(c);
        }
        if(p.containsKey(name)){
            try {
                if(arg!=null)
                    oc=p.get(name).invoke(o, arg);
                else
                    oc=p.get(name).invoke(o, (Object[])null);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                if(arg!=null)
                {
                    tp=new Class[arg.length];
                    for(int i=0;i<arg.length;i++){
                        tp[i]=arg[i].getClass();
                    }
                    f=c.getDeclaredMethod(name,tp);
                }
                else
                    f=c.getDeclaredMethod(name);
                f.setAccessible(true);
                p.put(name, f);
                if(arg!=null)
                    oc=p.get(name).invoke(o, arg);
                else
                    oc=p.get(name).invoke(o, (Object[])null);
            } catch (SecurityException |IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return oc;
    }
    public static Class<?> type[]={short.class,int.class,long.class,double.class,float.class};
    public static void test(){
        out.print(int.class);
        out.print(double.class);
        out.print(float.class);
        out.print(long.class);
    }
    
    public static int[] toIntArray(Object[] v){
        if(v.length==0)
            return (int[])null;
        int[] iv=new int[v.length];
        for(int i=0;i<v.length;i++)
            iv[i]=(int)v[i];
        return iv;
    }
    
    public static String[] toStringArray(Object[] v){
        if(v.length==0)
            return (String[])null;
        String[] iv=new String[v.length];
        for(int i=0;i<v.length;i++)
            iv[i]=(String)v[i];
        return iv;
    }
    
    public static Object toFloat(double v){
        return (float)v;
    }
    public static Object toByte(int v){
        return (Byte)(byte)v;
    }
    public static Object toLong(int v){
        return (long)v;
    }
    public static Object toShort(int v){
        return (short)v;
    }
    public static Object toChar(int v){
        return (char)v;
    }
    public static Object[] DataVal(Object[] arg){
        Object[] obj=null;
        int len=arg.length&~1;
        if(len<2)
            return obj;
        obj=new Object[len>>1];
        int count=0;
        for(int i=0;i<len;i++){
            switch((int)arg[i])
            {
                case 0:
                    Object c=null;
                    i++;
                    if(arg[i].getClass()==Double.class)
                    {
                        float fv=(float)((double)arg[i]);
                        obj[count]=fv;
                    }
                    else
                    {
                        obj[count]=Float.parseFloat(arg[i].toString());
                    }
                    count++;
                    break;
                case 1:
                    i++;
                    int si=(int)arg[i];
                    obj[count]=(short)si;
                    count++;
                    break;
                case 2:
                    i++;
                    int bv=(int)arg[i];
                    obj[count]=(byte)bv;
                    count++;
                    break;
                case 3:
                    i++;
                    obj[count]=(char)arg[i];
                    count++;
                    break;
                case 4:
                    i++;
                    obj[count]=(long)arg[i];
                    count++;
                    break;
                case 5:
                    i++;
                    obj[count]=(int)arg[i];
                    count++;
                    break;
                case 6:
                    i++;
                    obj[count]=(boolean)arg[i];
                    count++;
                    break;
                default:
                    i++;
                    obj[count]=arg[i];
                    count++;
                    break;
            }
        }
        //for(Object o:obj)
          //  out.print(o+"-"+o.getClass());
        return obj;
    }
    
    public static Object Invoke(String name,Object o,Class[] type,Object[] arg){
        Class c=o.getClass();
        Class[] tp;
        Object oc=null;
        if(!g_Classmap.containsKey(c.getName()))
            c=getClassName(c.getName());
        Map<String,Method> p;
        Method f=null;
        if(!g_Methodmap.containsKey(c)){
            p=new HashMap<>();
            g_Methodmap.put(c, p);
        }
        else{
            p=g_Methodmap.get(c);
        }
        if(p.containsKey(name)){
            try {
                if(arg!=null)
                    oc=p.get(name).invoke(o, arg);
                else
                    oc=p.get(name).invoke(o, (Object[])null);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            try {
                if(arg!=null)
                {
                    f=c.getDeclaredMethod(name,type);
                }
                else
                    f=c.getDeclaredMethod(name);
                f.setAccessible(true);
                p.put(name, f);
                if(arg!=null)
                    oc=p.get(name).invoke(o, arg);
                else
                    oc=p.get(name).invoke(o, (Object[])null);
            } catch (SecurityException |IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(Ref.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return oc;
    }
    
}
