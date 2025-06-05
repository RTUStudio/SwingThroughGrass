package com.github.ipecter.rtustudio.stg.listener;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import com.github.ipecter.rtustudio.stg.configuration.BlockConfig;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import kr.rtuserver.framework.bukkit.api.platform.MinecraftVersion;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

public class PlayerInteract extends RSListener<SwingThroughGrass> {

    private final BlockConfig blockConfig;
    private final boolean usePaperRayTrace;
    private final boolean isNewVersion;
    private final BlockCheckStrategy blockCheckStrategy;

    public PlayerInteract(SwingThroughGrass plugin) {
        super(plugin);
        this.blockConfig = plugin.getBlockConfig();
        boolean isPaper = MinecraftVersion.isPaper();
        this.usePaperRayTrace = isPaper && MinecraftVersion.isSupport("1.19.3");
        this.isNewVersion = MinecraftVersion.isSupport("1.21");
        
        // Selecting a block checking strategy based on server type
        this.blockCheckStrategy = isPaper ? 
            block -> block.getType().isSolid() || block.isCollidable() :
            block -> block.getType().isSolid() || !block.isPassable();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onSwing(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if (block == null) return;

        // Collision checking using the strategy
        if (!blockConfig.isCollidable() && blockCheckStrategy.shouldSkip(block)) {
            return;
        }

        // Optimized material inspection
        boolean inList = blockConfig.getMaterials().contains(block.getType());
        if (blockConfig.getMode() == BlockConfig.Mode.BLACKLIST ? inList : !inList) {
            return;
        }

        Player player = e.getPlayer();
        RayTraceResult result = performRayTrace(player);
        if (result == null) return;

        Entity hitEntity = result.getHitEntity();
        if (hitEntity != null) {
            handleEntityHit(e, player, hitEntity);
        }
    }

    private RayTraceResult performRayTrace(Player player) {
        if (usePaperRayTrace && !player.isInsideVehicle()) {
            return player.rayTraceEntities(3, true);
        }
        return player.getWorld().rayTrace(
            player.getEyeLocation(), 
            player.getLocation().getDirection(),
            3, FluidCollisionMode.NEVER, true, 0,
            entity -> entity != player
        );
    }

    private void handleEntityHit(PlayerInteractEvent originalEvent, Player player, Entity entity) {
        PlayerInteractEvent event = PlayerInteractEventFactory.create(isNewVersion, originalEvent, entity, player);
        if (callEvent(event)) {
            player.attack(entity);
            if (!blockConfig.isDestroy()) {
                originalEvent.setCancelled(true);
            }
        }
    }

    private boolean callEvent(PlayerInteractEvent event) {
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        return event.useItemInHand() != Event.Result.DENY;
    }

    @FunctionalInterface
    private interface BlockCheckStrategy {
        boolean shouldSkip(Block block);
    }
}
