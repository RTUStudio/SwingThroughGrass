package com.github.ipecter.rtustudio.stg.commands;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import com.github.ipecter.rtustudio.stg.configuration.BlockConfig;
import kr.rtuserver.framework.bukkit.api.command.RSCommand;
import kr.rtuserver.framework.bukkit.api.command.RSCommandData;

public class Command extends RSCommand {

    private final BlockConfig blockConfig;

    public Command(SwingThroughGrass plugin) {
        super(plugin, "stg", true);
        this.blockConfig = plugin.getBlockConfig();
    }

    @Override
    protected void reload(RSCommandData data) {
       blockConfig.reload();
    }

}
