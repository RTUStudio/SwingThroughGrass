package kr.rtustudio.stg.handler;

import kr.rtustudio.framework.bukkit.api.listener.RSListener;
import kr.rtustudio.framework.bukkit.api.platform.MinecraftVersion;
import kr.rtustudio.stg.SwingThroughGrass;
import kr.rtustudio.stg.configuration.BlockConfig;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.RayTraceResult;

@SuppressWarnings("unused")
public class PlayerInteract extends RSListener<SwingThroughGrass> {

    private final BlockConfig blockConfig;

    private final boolean isPaper = MinecraftVersion.isPaper();

    private final boolean newerVersion;

    public PlayerInteract(SwingThroughGrass plugin) {
        super(plugin);
        this.blockConfig = plugin.getConfiguration(BlockConfig.class);
        this.newerVersion = MinecraftVersion.isSupport("1.21");
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onSwing(PlayerInteractEvent e) {
        if (e.getAction() != Action.LEFT_CLICK_BLOCK) return;
        Block block = e.getClickedBlock();
        if (block == null) return;
        Material material = block.getType();

        if (!blockConfig.isCollidable()) {
            if (isPaper) {
                if (block.getType().isSolid() || block.isCollidable()) return;
            } else {
                if (block.getType().isSolid() || !block.isPassable()) return;
            }
        }

        boolean contains = blockConfig.getList().contains(material);
        if ((blockConfig.getMode() == BlockConfig.Mode.WHITELIST) != contains) return;

        Player player = e.getPlayer();

        RayTraceResult result;
        if (isPaper && !player.isInsideVehicle() && MinecraftVersion.isSupport("1.19.3")) {
            result = player.rayTraceEntities(3, true);
        } else {
            result = player.getWorld().rayTrace(player.getEyeLocation(), player.getLocation().getDirection(),
                    3, FluidCollisionMode.NEVER, true, 0,
                    entity -> entity != player);
        }
        if (result == null || result.getHitEntity() == null) return;
        
        PlayerInteractEvent event = PlayerInteractEventFactory.create(newerVersion, e, result, player);
        if (callEvent(event)) {
            player.attack(result.getHitEntity());
            if (!blockConfig.isDestroy()) e.setCancelled(true);
        }
    }

    private boolean callEvent(PlayerInteractEvent event) {
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        return event.useItemInHand() != Event.Result.DENY;
    }
}
