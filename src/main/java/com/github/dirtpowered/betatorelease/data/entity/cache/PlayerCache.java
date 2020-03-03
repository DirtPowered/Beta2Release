package com.github.dirtpowered.betatorelease.data.entity.cache;

import com.github.dirtpowered.betatorelease.data.entity.EntityPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerCache {
    private Map<UUID, EntityPlayer> players = new HashMap<>();

    public Map<UUID, EntityPlayer> getPlayers() {
        return players;
    }

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
