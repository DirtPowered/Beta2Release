package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b18;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.RespawnPacketData;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;

public class ServerRespawnTranslator implements ModernToBetaHandler<ServerRespawnPacket> {

    @Override
    public void translate(ServerRespawnPacket packet, Session betaSession) {
        BetaPlayer player = betaSession.getBetaPlayer();
        int dimension = packet.getDimension();

        int gameMode = switch (packet.getGameMode()) {
            case SURVIVAL, ADVENTURE -> 0;
            case CREATIVE, SPECTATOR -> 1;
        };

        int difficulty = switch (packet.getDifficulty()) {
            case PEACEFUL -> 0;
            case EASY -> 1;
            case NORMAL -> 2;
            case HARD -> 3;
        };

        player.setDimension(dimension);
        player.setGameMode(gameMode);
        betaSession.sendPacket(new RespawnPacketData((byte) dimension, difficulty, gameMode, 128, 0));
    }
}