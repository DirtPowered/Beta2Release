package com.github.dirtpowered.betatorelease.data.entity;

import com.github.dirtpowered.betatorelease.data.entity.model.Entity;

import java.util.UUID;

public class EntityPlayer extends Entity {

    private String username;

    public EntityPlayer(UUID uniqueId) {
        super(uniqueId);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
