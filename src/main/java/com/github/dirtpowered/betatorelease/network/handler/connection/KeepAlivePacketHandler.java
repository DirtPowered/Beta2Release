package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.KeepAlivePacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class KeepAlivePacketHandler implements BetaToModernHandler<KeepAlivePacketData> {

    @Override
    public void handlePacket(Session session, KeepAlivePacketData packetClass) {
        /* do nothing */
    }
}
