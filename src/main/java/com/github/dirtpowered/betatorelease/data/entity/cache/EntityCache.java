package com.github.dirtpowered.betatorelease.data.entity.cache;

import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import lombok.Getter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class EntityCache {
    private final Map<Integer, Entity> entities = new ConcurrentHashMap<>();

    public void addEntity(Entity entity) {
        entities.put(entity.getEntityId(), entity);
    }

    public Entity removeEntity(int entityId) {
        return entities.remove(entityId);
    }

    public Entity getEntityById(int entityId) {
        return entities.get(entityId);
    }
}