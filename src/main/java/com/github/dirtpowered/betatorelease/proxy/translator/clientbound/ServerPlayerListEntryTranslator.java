package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betatorelease.data.entity.EntityPlayer;
import com.github.dirtpowered.betatorelease.data.entity.cache.PlayerCache;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;

import java.util.UUID;

public class ServerPlayerListEntryTranslator implements ModernToBetaHandler<ServerPlayerListEntryPacket> {

    @Override
    public void translate(ServerPlayerListEntryPacket packet, Session betaSession) {
        refreshEntries(packet, betaSession);
    }

    private void refreshEntries(ServerPlayerListEntryPacket packet, Session betaSession) {
        PlayerCache cache = betaSession.getServer().getPlayerCache();

        for (PlayerListEntry entry : packet.getEntries()) {
            GameProfile gameProfile = entry.getProfile();
            UUID playerId = gameProfile.getId();

            switch (packet.getAction()) {
                case ADD_PLAYER -> {
                    EntityPlayer player = new EntityPlayer(playerId);
                    player.setUsername(gameProfile.getName());
                    cache.addPlayer(player);
                }
                case REMOVE_PLAYER -> cache.removePlayer(playerId);
            }
        }
    }
}