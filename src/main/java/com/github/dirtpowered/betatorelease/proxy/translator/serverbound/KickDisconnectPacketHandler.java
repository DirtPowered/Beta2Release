package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3KickDisconnectPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class KickDisconnectPacketHandler implements BetaToModernHandler<V1_7_3KickDisconnectPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3KickDisconnectPacketData packetClass) {
        session.getModernClient().disconnect(packetClass.getDisconnectReason());
    }
}