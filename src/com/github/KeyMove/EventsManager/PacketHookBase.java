/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove.EventsManager;

import static java.lang.System.out;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaFunction;

/**
 *
 * @author Administrator
 */
public class PacketHookBase {
    public List<LuaFunction> CallBack=new ArrayList<>();
    
    public void HookPlayer(Player p){
        out.print("233");
    }
    
    public void unregister(){
        CallBack.clear();
    }
    
    public int AddFunction(LuaFunction function){
        CallBack.add(function);
        return CallBack.size()-1;
    }
    
    public void RemoveFunction(int Index){
        CallBack.remove(Index);
    }
}
