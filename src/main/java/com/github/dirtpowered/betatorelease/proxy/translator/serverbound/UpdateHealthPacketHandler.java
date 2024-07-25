package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3UpdateHealthPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class UpdateHealthPacketHandler implements BetaToModernHandler<V1_7_3UpdateHealthPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3UpdateHealthPacketData packetClass) {
        // do nothing
    }
}