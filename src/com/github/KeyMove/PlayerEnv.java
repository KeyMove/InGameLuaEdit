/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 *
 * @author Administrator
 */
public class PlayerEnv implements Listener{
    Player TargetPlayer;

    public PlayerEnv(Player p) {
        TargetPlayer=p;
    }
    
    @EventHandler void Say(AsyncPlayerChatEvent e){
        
    }
    
    @EventHandler void Tab(PlayerChatTabCompleteEvent e){
        
    }
    
    @EventHandler void RightClick(PlayerInteractEvent e){
        
    }
    
    @EventHandler void RigthClickEntity(PlayerInteractEntityEvent e){
        
    }
        
}
