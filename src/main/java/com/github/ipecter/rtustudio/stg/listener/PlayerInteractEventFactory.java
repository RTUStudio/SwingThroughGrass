package com.github.ipecter.rtustudio.stg.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PlayerInteractEventFactory {

    private static final Constructor<? extends PlayerInteractEvent> CONSTRUCTOR;
    private static final boolean IS_NEW_API;
    private static final BlockFace DEFAULT_BLOCK_FACE = BlockFace.SOUTH;
    private static final Action DEFAULT_ACTION = Action.LEFT_CLICK_AIR;

    static {
        Constructor<? extends PlayerInteractEvent> newConstructor = null;
        boolean isNewApi = false;
        
        try {
            newConstructor = PlayerInteractEvent.class.getConstructor(
                Player.class, Action.class, ItemStack.class, Block.class, 
                BlockFace.class, EquipmentSlot.class, Vector.class
            );
            isNewApi = true;
        } catch (NoSuchMethodException ignored) {}
        
        if (newConstructor == null) {
            try {
                newConstructor = PlayerInteractEvent.class.getConstructor(
                    Player.class, Action.class, ItemStack.class, Block.class, 
                    BlockFace.class, EquipmentSlot.class, Location.class
                );
            } catch (NoSuchMethodException ex) {
                throw new IllegalStateException("No valid PlayerInteractEvent constructor found", ex);
            }
        }
        
        CONSTRUCTOR = newConstructor;
        IS_NEW_API = isNewApi;
    }

    public static PlayerInteractEvent create(PlayerInteractEvent originalEvent, RayTraceResult rayTraceResult) {
        try {
            final Player player = originalEvent.getPlayer();
            final Object locationParam = IS_NEW_API ? 
                rayTraceResult.getHitPosition() : 
                rayTraceResult.getHitPosition().toLocation(player.getWorld());
            
            return CONSTRUCTOR.newInstance(
                player,
                DEFAULT_ACTION,
                originalEvent.getItem(),
                null,
                DEFAULT_BLOCK_FACE,
                originalEvent.getHand(),
                locationParam
            );
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            throw new RuntimeException("Failed to create PlayerInteractEvent", ex);
        }
    }
}
