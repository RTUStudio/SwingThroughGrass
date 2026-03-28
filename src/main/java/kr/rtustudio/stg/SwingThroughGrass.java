package kr.rtustudio.stg;

import kr.rtustudio.framework.bukkit.api.RSPlugin;
import kr.rtustudio.configurate.model.ConfigPath;
import kr.rtustudio.stg.command.MainCommand;
import kr.rtustudio.stg.configuration.BlockConfig;
import kr.rtustudio.stg.configuration.serializer.MaterialSerializer;
import kr.rtustudio.stg.handler.PlayerInteract;
import lombok.Getter;
import org.bukkit.Material;

public class SwingThroughGrass extends RSPlugin {

    @Getter
    private static SwingThroughGrass instance;

    @Override
    protected void load() {
        instance = this;
    }

    @Override
    protected void enable() {

        registerConfiguration(BlockConfig.class, ConfigPath.of("Block"), builder -> {
            builder.register(Material.class, new MaterialSerializer());
        });

        registerEvent(new PlayerInteract(this));
        registerCommand(new MainCommand(this), true);
    }
}
