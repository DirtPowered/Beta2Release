package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.UpdateProgressPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;

public class ServerWindowPropertyTranslator implements ModernToBetaHandler<ServerWindowPropertyPacket> {

    @Override
    public void translate(ServerWindowPropertyPacket packet, Session betaSession) {
        int progressbar = packet.getRawProperty();
        int progressbarValue = progressbar == 0 ? (packet.getValue() * 200) / 1600 : packet.getValue();
        //TODO: Furnace cache (fuel time value)
        betaSession.sendPacket(new UpdateProgressPacketData(0 /* always 0 */, progressbar == 0 ? 1 : 0, progressbarValue));
    }
}
