package com.github.dirtpowered.betatorelease.data.entity.cache;

import com.github.dirtpowered.betatorelease.data.entity.EntityPlayer;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerCache {
    private final Map<UUID, EntityPlayer> players = new HashMap<>();

    public void addPlayer(EntityPlayer player) {
        players.put(player.getUniqueId(), player);
    }

    public void removePlayer(UUID uniqueId) {
        players.remove(uniqueId);
    }

    public EntityPlayer getPlayerFromUUID(UUID uniqueId) {
        return players.get(uniqueId);
    }
}