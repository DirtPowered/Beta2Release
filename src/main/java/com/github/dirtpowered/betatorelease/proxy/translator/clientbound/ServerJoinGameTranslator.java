package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3LoginPacketData;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;

public class ServerJoinGameTranslator implements ModernToBetaHandler<ServerJoinGamePacket> {

    @Override
    public void translate(ServerJoinGamePacket packet, Session betaSession) {
        BetaPlayer betaPlayer = betaSession.getBetaPlayer();
        int entityId = packet.getEntityId();
        int dimension = packet.getDimension();

        Main.LOGGER.info("Entity player joined (modern server) with id {}", entityId);
        betaPlayer.setEntityId(entityId);
        betaPlayer.setDimension(dimension);

        long seed = betaSession.getServer().getConfiguration().getWorldSeed();
        betaSession.sendPacket(new V1_7_3LoginPacketData(entityId, "", seed, dimension));
    }
}