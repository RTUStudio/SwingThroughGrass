package com.github.ipecter.rtustudio.stg.configuration;

import com.github.ipecter.rtustudio.stg.SwingThroughGrass;
import kr.rtuserver.framework.bukkit.api.configuration.RSConfiguration;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class BlockConfig extends RSConfiguration<SwingThroughGrass> {

    private final List<Material> materials = new ArrayList<>();
    private boolean collidable = false;
    private boolean destroy = false;
    private Mode mode;

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
        try {
            mode = Mode.valueOf(modeStr);
        } catch (IllegalArgumentException e) {
            mode = Mode.BLACKLIST;
        }

        List<String> configList = getStringList("list", List.of(), """
                whitelist or blacklist of material
                메터리얼의 화이트리스트 또는 블랙리스트""");
        
        Set<String> validMaterialNames = EnumSet.allOf(Material.class)
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
        
        List<String> invalidMaterials = new ArrayList<>();
        materials.clear();
        
        for (String name : configList) {
            String normalized = name.toUpperCase();
            if (validMaterialNames.contains(normalized)) {
                materials.add(Material.valueOf(normalized));
            } else {
                invalidMaterials.add(name);
            }
        }
        
        if (!invalidMaterials.isEmpty()) {
            String errorMsg = "Invalid materials: " + String.join(", ", invalidMaterials);
            String koreanMsg = "올바르지 않은 메터리얼: " + String.join(", ", invalidMaterials);
            getPlugin().console("<red>" + errorMsg + "</red>");
            getPlugin().console("<red>" + koreanMsg + "</red>");
        }
    }

    public enum Mode {
        WHITELIST,
        BLACKLIST
    }
}
