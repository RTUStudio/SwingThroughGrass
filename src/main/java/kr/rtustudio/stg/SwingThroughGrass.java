package kr.rtustudio.stg;

import kr.rtustudio.framework.bukkit.api.RSPlugin;
import kr.rtustudio.stg.command.MainCommand;
import kr.rtustudio.stg.configuration.BlockConfig;
import kr.rtustudio.stg.configuration.serializer.MaterialSerializer;
import kr.rtustudio.stg.listener.PlayerInteract;
import lombok.Getter;
import org.bukkit.Material;

public class SwingThroughGrass extends RSPlugin {

    @Getter
    private static SwingThroughGrass instance;

    @Override
    public void enable() {
        instance = this;

        registerConfiguration(BlockConfig.class, "Block", builder -> {
            builder.register(Material.class, new MaterialSerializer());
        });

        registerEvent(new PlayerInteract(this));
        registerCommand(new MainCommand(this), true);
    }
}
