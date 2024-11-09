package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.ResourcePackStatus;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientResourcePackStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerResourcePackSendPacket;

public class ServerResourcePackSendTranslator implements ModernToBetaHandler<ServerResourcePackSendPacket> {

    @Override
    public void translate(ServerResourcePackSendPacket packet, Session betaSession) {
        if (packet.getUrl().isEmpty() || packet.getHash().isEmpty())
            return;

        betaSession.getModernClient().sendModernPacket(new ClientResourcePackStatusPacket(ResourcePackStatus.ACCEPTED));
        betaSession.getModernClient().sendModernPacket(new ClientResourcePackStatusPacket(ResourcePackStatus.SUCCESSFULLY_LOADED));
    }
}