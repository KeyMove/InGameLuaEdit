/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.github.KeyMove;

import static java.lang.System.out;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInAbilities;
import net.minecraft.server.v1_8_R3.PacketPlayInArmAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockPlace;
import net.minecraft.server.v1_8_R3.PacketPlayInChat;
import net.minecraft.server.v1_8_R3.PacketPlayInClientCommand;
import net.minecraft.server.v1_8_R3.PacketPlayInCloseWindow;
import net.minecraft.server.v1_8_R3.PacketPlayInCustomPayload;
import net.minecraft.server.v1_8_R3.PacketPlayInEnchantItem;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInHeldItemSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInKeepAlive;
import net.minecraft.server.v1_8_R3.PacketPlayInResourcePackStatus;
import net.minecraft.server.v1_8_R3.PacketPlayInSetCreativeSlot;
import net.minecraft.server.v1_8_R3.PacketPlayInSettings;
import net.minecraft.server.v1_8_R3.PacketPlayInSpectate;
import net.minecraft.server.v1_8_R3.PacketPlayInSteerVehicle;
import net.minecraft.server.v1_8_R3.PacketPlayInTabComplete;
import net.minecraft.server.v1_8_R3.PacketPlayInTransaction;
import net.minecraft.server.v1_8_R3.PacketPlayInUpdateSign;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.minecraft.server.v1_8_R3.PacketPlayInWindowClick;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 *
 * @author Administrator
 */
public class PacketHook implements PacketListenerPlayIn{
    PlayerConnection OldListener;
    public PacketHook(Player p) {
        OldListener=((CraftPlayer)p).getHandle().playerConnection;
        OldListener.networkManager.a(this);
    }
    
    public void unregister(){
        OldListener.networkManager.a(OldListener);
    }
    
    void MessageOut(Object e){
        out.print("监控到入口包："+e.getClass().getName());
    }
    
    @Override
    public void a(PacketPlayInArmAnimation e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInChat e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInTabComplete e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInClientCommand e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSettings e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInTransaction e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInEnchantItem e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInWindowClick e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInCloseWindow e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInCustomPayload e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInUseEntity e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInKeepAlive e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInFlying e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInAbilities e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInBlockDig e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInEntityAction e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSteerVehicle e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInHeldItemSlot e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSetCreativeSlot e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInUpdateSign e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInBlockPlace e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSpectate e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInResourcePackStatus e) {
        MessageOut(e);
        OldListener.a(e);
    }

    @Override
    public void a(IChatBaseComponent e) {
        MessageOut(e);
        OldListener.a(e);
    }
    
}
