package com.github.dirtpowered.betatorelease.data.entity.cache;

import com.github.dirtpowered.betatorelease.data.entity.model.Entity;

import java.util.HashMap;
import java.util.Map;

public class EntityCache {
    private Map<Integer, Entity> entities = new HashMap<>();

    public Map<Integer, Entity> getEntities() {
        return entities;
    }

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
