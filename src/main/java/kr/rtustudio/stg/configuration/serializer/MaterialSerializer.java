package kr.rtustudio.stg.configuration.serializer;

import kr.rtustudio.configurate.ConfigurationNode;
import kr.rtustudio.configurate.serialize.SerializationException;
import kr.rtustudio.configurate.serialize.TypeSerializer;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class MaterialSerializer implements TypeSerializer<Material> {

    @Override
    public Material deserialize(Type type, ConfigurationNode node) throws SerializationException {
        String id = node.getString();
        if (id == null) throw new SerializationException(node, type, "Missing ID");
        id = id.contains(":") ? id : NamespacedKey.MINECRAFT + ":" + id;
        NamespacedKey key;
        try {
            key = NamespacedKey.fromString(id.toLowerCase());
        } catch (Exception e) {
            throw new SerializationException(node, type, "Invalid ID: " + id, e);
        }
        if (key == null) return null;
        Material material = Registry.MATERIAL.get(key);
        if (material == null) return null;
        if (!material.isBlock()) throw new SerializationException(node, type, "Material is not a block: " + id);
        return material;
    }

    @Override
    public void serialize(Type type, @Nullable Material obj, ConfigurationNode node) throws SerializationException {
        if (obj == null) {
            node.set(null);
            return;
        }
        node.set(obj.getKey().toString());
    }
}
