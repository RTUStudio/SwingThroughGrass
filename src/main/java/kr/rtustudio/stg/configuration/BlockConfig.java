package kr.rtustudio.stg.configuration;

import kr.rtustudio.configurate.model.ConfigurationPart;
import kr.rtustudio.configurate.objectmapping.meta.Comment;
import lombok.Getter;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

@Getter
@SuppressWarnings({
        "unused",
        "CanBeFinal",
        "FieldCanBeLocal",
        "FieldMayBeFinal",
        "InnerClassMayBeStatic"
})
public class BlockConfig extends ConfigurationPart {

    @Comment("""
            Allow ignoring collidable blocks
            충돌가능한 블럭을 무시하도록 허용합니다""")
    private boolean collidable = false;

    @Comment("""
            Break block when player swings
            플레이어가 공격시 블럭을 파괴합니다""")
    private boolean destroy = true;

    private Mode mode = Mode.BLACKLIST;

    @Comment("""
            Material: https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/Material.html
            
            whitelist or blacklist of block material
            블럭 메터리얼 화이트리스트 또는 블랙리스트
            
            You can omit the namespace if it is minecraft
            네임스페이스가 minecraft인 경우 생략할 수 있습니다""")
    private List<Material> list = new ArrayList<>();

    public enum Mode {
        WHITELIST,
        BLACKLIST
    }
}
