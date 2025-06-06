package com.github.ipecter.rtustudio.stg.listener;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import com.github.ipecter.rtustudio.stg.configuration.BlockConfig;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import kr.rtuserver.framework.bukkit.api.platform.MinecraftVersion;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

public class PlayerInteract extends RSListener<SwingThroughGrass> {

    private final BlockConfig blockConfig;

    private final boolean isPaper = MinecraftVersion.isPaper();

    private final boolean newerVersion;

    public PlayerInteract(SwingThroughGrass plugin) {
        super(plugin);
        this.blockConfig = plugin.getBlockConfig();
        this.newerVersion = MinecraftVersion.isSupport("1.21");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onSwing(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if (block == null) return;

        if (!blockConfig.isCollidable()) {
            if (isPaper) {
                if (block.getType().isSolid() || block.isCollidable()) return;
            } else {
                if (block.getType().isSolid() || !block.isPassable()) return;
            }
        }

        boolean pass = blockConfig.getMaterials().contains(block.getType());
        if (blockConfig.getMode() == BlockConfig.Mode.BLACKLIST) pass = !pass;
        if (!pass) return;

        Player player = e.getPlayer();
        RayTraceResult result;
        if (isPaper && !player.isInsideVehicle() && MinecraftVersion.isSupport("1.19.3")) {
            result = player.rayTraceEntities(3, true);
        } else {
            result = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(),
                    3, FluidCollisionMode.NEVER, true, 0,
                    entity -> entity != player);
        }
        if (result == null) return;
        if (result.getHitEntity() != null) {
            PlayerInteractEvent event = PlayerInteractEventFactory.create(newerVersion, e, result, player);
            if (callEvent(event)) {
                player.attack(result.getHitEntity());
                if (!blockConfig.isDestroy()) e.setCancelled(true);
            }
        }
    }

    private boolean callEvent(PlayerInteractEvent event) {
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        return event.useItemInHand() != Event.Result.DENY;
    }
}
