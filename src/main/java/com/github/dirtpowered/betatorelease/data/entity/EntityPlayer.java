package com.github.dirtpowered.betatorelease.data.entity;

import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class EntityPlayer extends Entity {
    private String username;

    public EntityPlayer(UUID uniqueId) {
        super(uniqueId);
    }

    public EntityPlayer(int entityId) {
        super(entityId);
    }
}