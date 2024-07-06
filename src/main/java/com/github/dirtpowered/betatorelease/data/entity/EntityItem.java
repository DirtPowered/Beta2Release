package com.github.dirtpowered.betatorelease.data.entity;

import com.github.dirtpowered.betaprotocollib.data.BetaItemStack;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EntityItem extends Entity {
    private float rotation;
    private float pitch;
    private float roll;
    private BetaItemStack itemStack;

    public EntityItem(int entityId) {
        super(entityId);
    }
}