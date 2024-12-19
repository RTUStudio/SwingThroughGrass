package com.github.ipecter.rtustudio.stg.configuration;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import kr.rtuserver.framework.bukkit.api.config.RSConfiguration;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlockConfig extends RSConfiguration {

    private final SwingThroughGrass plugin;

    private boolean destroy = false;

    private Mode mode;

    private final List<Material> materials = new ArrayList<>();

    public BlockConfig(SwingThroughGrass plugin) {
        super(plugin, "Block.yml", null);
        this.plugin = plugin;
        setup(this);
    }

    private void init() {
        destroy = getBoolean("destroy", false, """
                Break block when player swings
                플레이어가 공격시 블럭을 파괴합니다""");
        String modeStr = getString("mode", "BLACKLIST", """
                WHITELIST / BLACKLIST""").toUpperCase();
        if (List.of("WHITELIST", "BLACKLIST").contains(modeStr)) mode = Mode.valueOf(modeStr);
        else mode = Mode.BLACKLIST;
        List<String> list = getStringList("list", List.of(), """
                whitelist or blacklist of material
                메터리얼의 화이트리스트 또는 블랙리스트""");
        for (String str : list) {
            Material material = Material.getMaterial(str);
            if (material != null) materials.add(material);
            else {
                plugin.console("<red>" + str + " is not a valid material</red>");
                plugin.console("<red>" + str + "은(는) 올바른 메터리얼이 아닙니다</red>");
            }
        }
    }

    public enum Mode {
        WHITELIST,
        BLACKLIST
    }
}
