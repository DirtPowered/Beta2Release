package com.github.dirtpowered.betatorelease.data.entity.model;

import com.github.dirtpowered.betaprotocollib.utils.Location;

import java.util.UUID;

public abstract class Entity {

    private int entityId;
    private UUID uniqueId;
    private Location location;

    public Entity(int entityId) {
        this.entityId = entityId;
    }

    public Entity(UUID uuid) {
        this.uniqueId = uuid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getEntityId() {
        return entityId;
    }

    public UUID getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }
}
