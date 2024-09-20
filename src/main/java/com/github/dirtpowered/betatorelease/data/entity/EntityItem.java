package com.github.dirtpowered.betatorelease.data.entity;

import com.github.dirtpowered.betaprotocollib.data.BetaItemStack;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Setter
@Getter
public class EntityItem extends Entity {
    private float rotation;
    private float pitch;
    private float roll;
    private BetaItemStack itemStack;
    private boolean tickable = false;
    private Consumer<EntityItem> tickConsumer;

    private int velocityX;
    private int velocityY;
    private int velocityZ;

    public EntityItem(int entityId) {
        super(entityId);
    }
}