package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.BedAndWeatherPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;

public class ServerNotifyClientTranslator implements ModernToBetaHandler<ServerNotifyClientPacket> {

    @Override
    public void translate(ServerNotifyClientPacket packet, Session betaSession) {
        int state;

        switch (packet.getNotification()) {
            case START_RAIN:
                state = 1;
                break;
            case STOP_RAIN:
                state = 2;
                break;
            case INVALID_BED:
                state = 0;
                break;
            default:
                state = -1;
                break;
        }

        if (state == -1)
            return;

        betaSession.sendPacket(new BedAndWeatherPacketData(state));
    }
}
