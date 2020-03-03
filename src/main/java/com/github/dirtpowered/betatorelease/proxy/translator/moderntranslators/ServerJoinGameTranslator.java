package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.LoginPacketData;
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
        betaPlayer.setEntityId(entityId);

        betaSession.sendPacket(new LoginPacketData(entityId, "", 0, dimension));
    }
}
