package com.github.ipecter.rtustudio.stg.command;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import com.github.ipecter.rtustudio.stg.configuration.BlockConfig;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;

public final class MainCommand extends RSCommand<SwingThroughGrass> {

    private final BlockConfig blockConfig;

    public MainCommand(SwingThroughGrass plugin) {
        super(plugin, "stg");
        this.blockConfig = plugin.getBlockConfig();
    }

    @Override
    protected void reload(RSCommandData data) {
        blockConfig.reload();
    }
}
