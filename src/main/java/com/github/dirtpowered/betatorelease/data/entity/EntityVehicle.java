package com.github.dirtpowered.betatorelease.data.entity;

import com.github.dirtpowered.betatorelease.data.entity.model.Entity;

public class EntityVehicle extends Entity {
    private int passenger;

    public EntityVehicle(int entityId) {
        super(entityId);
    }

    public int getPassenger() {
        return passenger;
    }

    public void setPassenger(int entityId) {
        passenger = entityId;
    }
}
