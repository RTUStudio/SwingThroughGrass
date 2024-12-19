package com.github.ipecter.rtustudio.stg.listeners;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import com.github.ipecter.rtustudio.stg.configuration.BlockConfig;
import kr.rtuserver.framework.bukkit.api.listener.RSListener;
import org.bukkit.FluidCollisionMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

public class PlayerInteract extends RSListener {

    private final BlockConfig blockConfig;

    public PlayerInteract(SwingThroughGrass plugin) {
        super(plugin);
        this.blockConfig = plugin.getBlockConfig();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSwing(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            Block block = e.getClickedBlock();
            if (block == null) return;
            if (block.getState().isCollidable()) return;

            boolean pass;
            if (blockConfig.getMode() == BlockConfig.Mode.WHITELIST) {
                pass = blockConfig.getMaterials().contains(block.getType());
            } else {
                pass = !blockConfig.getMaterials().contains(block.getType());
            }
            if (!pass) return;

            Player player = e.getPlayer();
            RayTraceResult result = player.getWorld().rayTrace(player.getEyeLocation(),
                    player.getLocation().getDirection(),
                    3.0,
                    FluidCollisionMode.NEVER,
                    true,
                    0.5, null);
            if (result != null && result.getHitEntity() != null) {
                player.attack(result.getHitEntity());
                if (!blockConfig.isDestroy()) e.setCancelled(true);
            }
        }
    }
}
