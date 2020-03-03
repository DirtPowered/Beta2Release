package com.github.dirtpowered.betatorelease.data.entity;

import com.github.dirtpowered.betaprotocollib.utils.BetaItemStack;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;

public class EntityItem extends Entity {

    private float rotation;
    private float pitch;
    private float roll;
    private BetaItemStack itemStack;

    public EntityItem(int entityId) {
        super(entityId);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getRoll() {
        return roll;
    }

    public void setRoll(float roll) {
        this.roll = roll;
    }

    public BetaItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(BetaItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
