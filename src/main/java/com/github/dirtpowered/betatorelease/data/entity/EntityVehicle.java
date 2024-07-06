package com.github.dirtpowered.betatorelease.data.entity;

import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityVehicle extends Entity {
    private int passenger;

    public EntityVehicle(int entityId) {
        super(entityId);
    }
}