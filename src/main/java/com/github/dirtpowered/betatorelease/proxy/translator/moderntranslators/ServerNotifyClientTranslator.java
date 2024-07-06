package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BedAndWeatherPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;

public class ServerNotifyClientTranslator implements ModernToBetaHandler<ServerNotifyClientPacket> {

    @Override
    public void translate(ServerNotifyClientPacket packet, Session betaSession) {
        int state = switch (packet.getNotification()) {
            case START_RAIN -> 2;
            case STOP_RAIN -> 1;
            case INVALID_BED -> 0;
            default -> -1;
        };

        if (state == -1)
            return;

        betaSession.sendPacket(new BedAndWeatherPacketData(state));
    }
}
