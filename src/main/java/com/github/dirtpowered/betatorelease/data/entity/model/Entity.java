package com.github.dirtpowered.betatorelease.data.entity.model;

import com.github.dirtpowered.betaprotocollib.utils.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public abstract class Entity {
    private int entityId;
    private UUID uniqueId;

    @Setter
    private Location location;

    public Entity(int entityId) {
        this.entityId = entityId;
    }

    public Entity(UUID uuid) {
        this.uniqueId = uuid;
    }
}