

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import com.github.KeyMove.EventsManager.PacketHookBase;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

/**
 *
 * @author Administrator
 */
public class PacketHook extends PacketHookBase implements PacketListenerPlayIn{
    
    PlayerConnection OldListener;
    @Override
    public void HookPlayer(Player p){
        savePlayer=p;
        OldListener=((CraftPlayer)p).getHandle().playerConnection;
        OldListener.networkManager.a(this);
    }

    @Override
    public void unregister() {
        OldListener.networkManager.a(OldListener);
        super.unregister(); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    boolean MessageOut(Object e){
        String Name=e.getClass().getName();
        Name=Name.substring(Name.lastIndexOf('.')+1);
        //out.print("Input Packet:"+Name);
        for(LuaFunction function:CallBack){
            if(!function.call(CoerceJavaToLua.coerce(Name),CoerceJavaToLua.coerce(savePlayer),CoerceJavaToLua.coerce(e)).isnil())
            {
                return false;
            }
        }
        return true;
    }
    
    public void a(Object... arg){
        MessageOut(arg[0]);
    }
    
    public void a(Object e){
        MessageOut(e);
    }
    
    public void a(){
    }
    
    @Override
    public void a(PacketPlayInArmAnimation e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInChat e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInTabComplete e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInClientCommand e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSettings e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInTransaction e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInEnchantItem e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInWindowClick e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInCloseWindow e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInCustomPayload e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInUseEntity e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInKeepAlive e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInFlying e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInAbilities e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInBlockDig e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInEntityAction e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSteerVehicle e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInHeldItemSlot e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSetCreativeSlot e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInUpdateSign e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInBlockPlace e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInSpectate e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(PacketPlayInResourcePackStatus e) {
        if(MessageOut(e))
            OldListener.a(e);
    }

    @Override
    public void a(IChatBaseComponent e) {
        if(MessageOut(e))
            OldListener.a(e);
    }
    
}
