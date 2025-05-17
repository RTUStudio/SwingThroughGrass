package com.github.ipecter.rtustudio.stg.listener;

import org.bukkit.block.Block;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.bukkit.Location;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PlayerInteractEventFactory {

    private static Constructor<?> constructor;

    static {
        try {
            Class<?> clazz = PlayerInteractEvent.class;
            Class<?>[] newerParams = new Class<?>[] {
                    Player.class,
                    Action.class,
                    ItemStack.class,
                    Block.class,
                    BlockFace.class,
                    EquipmentSlot.class,
                    Vector.class
            };
            constructor = clazz.getConstructor(newerParams);
        } catch (NoSuchMethodException ignored) {
            Class<?> clazz = PlayerInteractEvent.class;
            try {
                Class<?>[] olderParams = new Class<?>[] {
                        Player.class,
                        Action.class,
                        ItemStack.class,
                        Block.class,
                        BlockFace.class,
                        EquipmentSlot.class,
                        Location.class
                };
                constructor = clazz.getConstructor(olderParams);
            } catch (NoSuchMethodException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static PlayerInteractEvent create(boolean newerVersion, PlayerInteractEvent e, RayTraceResult result, Player player) {
        try {
            Object[] params;
            if (newerVersion) {
                params = new Object[]{e.getPlayer(), Action.LEFT_CLICK_AIR, e.getItem(), null, BlockFace.SOUTH, e.getHand(), result.getHitPosition()};
            } else {
                params = new Object[]{e.getPlayer(), Action.LEFT_CLICK_AIR, e.getItem(), null, BlockFace.SOUTH, e.getHand(), result.getHitPosition().toLocation(player.getWorld())};
            }
            return (PlayerInteractEvent) constructor.newInstance(params);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
