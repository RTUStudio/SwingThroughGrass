package com.github.ipecter.rtustudio.stg;

import com.github.ipecter.rtustudio.stg.command.MainCommand;
import com.github.ipecter.rtustudio.stg.configuration.BlockConfig;
import com.github.ipecter.rtustudio.stg.listener.PlayerInteract;
import kr.rtuserver.framework.bukkit.api.RSPlugin;
import org.jetbrains.annotations.NotNull;

public final class SwingThroughGrass extends RSPlugin {

    private static SwingThroughGrass instance;
    private final BlockConfig blockConfig;

    public SwingThroughGrass() {
        this.blockConfig = new BlockConfig(this);
    }

    @Override
    public void enable() {
        instance = this;

        // Register event listener
        registerEvent(new PlayerInteract(this));

        // Register command with auto-completion
        registerCommand(new MainCommand(this), true);
    }

    public static @NotNull SwingThroughGrass getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Plugin not initialized yet!");
        }
        return instance;
    }

    public @NotNull BlockConfig getBlockConfig() {
        return blockConfig;
    }
}
