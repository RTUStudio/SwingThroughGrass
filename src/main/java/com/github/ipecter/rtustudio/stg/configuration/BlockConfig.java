package com.github.ipecter.rtustudio.stg.configuration;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import kr.rtuserver.framework.bukkit.api.config.RSConfiguration;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlockConfig extends RSConfiguration<SwingThroughGrass> {

    private boolean collidable = false;

    private boolean destroy = false;

    private Mode mode;

    private final List<Material> materials = new ArrayList<>();

    public BlockConfig(SwingThroughGrass plugin) {
        super(plugin, "Block.yml", null);
        setup(this);
    }

    private void init() {
        collidable = getBoolean("collidable", collidable, """
                Allow ignoring collidable blocks
                충돌가능한 블럭을 무시하도록 허용합니다""");
        destroy = getBoolean("destroy", destroy, """
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
                getPlugin().console("<red>" + str + " is not a valid material</red>");
                getPlugin().console("<red>" + str + "은(는) 올바른 메터리얼이 아닙니다</red>");
            }
        }
    }

    public enum Mode {
        WHITELIST,
        BLACKLIST
    }
}
