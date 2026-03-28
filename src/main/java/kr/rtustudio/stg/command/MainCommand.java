package kr.rtustudio.stg.command;

import kr.rtustudio.stg.SwingThroughGrass;
import kr.rtustudio.stg.configuration.BlockConfig;
import kr.rtustudio.framework.bukkit.api.command.RSCommand;
import kr.rtustudio.framework.bukkit.api.command.CommandArgs;
import org.bukkit.permissions.PermissionDefault;

public class MainCommand extends RSCommand<SwingThroughGrass> {

    public MainCommand(SwingThroughGrass plugin) {
        super(plugin, "stg", PermissionDefault.OP);
    }

    @Override
    protected void reload(CommandArgs args) {
        plugin.reloadConfiguration(BlockConfig.class);
    }

}
