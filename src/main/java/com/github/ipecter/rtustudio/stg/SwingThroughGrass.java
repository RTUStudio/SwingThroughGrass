package com.github.ipecter.rtustudio.stg;

import com.github.ipecter.rtustudio.stg.command.MainCommand;
import com.github.ipecter.rtustudio.stg.configuration.BlockConfig;
import com.github.ipecter.rtustudio.stg.listener.PlayerInteract;
import kr.rtuserver.framework.bukkit.api.RSPlugin;
import lombok.Getter;

public class SwingThroughGrass extends RSPlugin {

    @Getter
    private static SwingThroughGrass instance;
    @Getter
    private BlockConfig blockConfig;

    @Override
    public void enable() {
        instance = this;

        blockConfig = new BlockConfig(this);

        registerEvent(new PlayerInteract(this));
        registerCommand(new MainCommand(this), true);
    }
}
