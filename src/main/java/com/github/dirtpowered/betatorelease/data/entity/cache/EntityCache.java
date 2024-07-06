package com.github.dirtpowered.betatorelease.data.entity.cache;

import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class EntityCache {
    private final Map<Integer, Entity> entities = new HashMap<>();

    public void addEntity(Entity entity) {
        entities.put(entity.getEntityId(), entity);
    }

    public void removeEntity(int entityId) {
        entities.remove(entityId);
    }

    public Entity getEntityById(int entityId) {
        return entities.get(entityId);
    }
}