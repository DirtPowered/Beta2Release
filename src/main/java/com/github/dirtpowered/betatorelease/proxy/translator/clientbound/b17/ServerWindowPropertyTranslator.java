package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UpdateProgressPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;

public class ServerWindowPropertyTranslator implements ModernToBetaHandler<ServerWindowPropertyPacket> {

    @Override
    public void translate(ServerWindowPropertyPacket packet, Session betaSession) {
        int progressbar = packet.getRawProperty();
        int progressbarValue = progressbar == 0 ? (packet.getValue() * 200) / 1600 : packet.getValue();

        betaSession.sendPacket(new UpdateProgressPacketData(packet.getWindowId(), progressbar == 0 ? 1 : 0, progressbarValue));
    }
}