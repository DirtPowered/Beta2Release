package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b18;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.LoginPacketData;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import org.pmw.tinylog.Logger;

public class ServerJoinGameTranslator implements ModernToBetaHandler<ServerJoinGamePacket> {

    @Override
    public void translate(ServerJoinGamePacket packet, Session betaSession) {
        BetaPlayer betaPlayer = betaSession.getBetaPlayer();
        int entityId = packet.getEntityId();
        int dimension = packet.getDimension();

        Logger.info("Entity player joined (modern server) with id {}", entityId);

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

        betaPlayer.setEntityId(entityId);
        betaPlayer.setDimension(dimension);
        betaPlayer.setGameMode(gameMode);

        betaSession.sendPacket(new LoginPacketData(entityId, "", 0, gameMode, dimension, difficulty, 128, packet.getMaxPlayers()));
    }
}