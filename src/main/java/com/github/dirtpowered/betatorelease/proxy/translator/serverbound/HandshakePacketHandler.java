package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.HandshakePacketData;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class HandshakePacketHandler implements BetaToModernHandler<HandshakePacketData> {

    @Override
    public void handlePacket(Session session, HandshakePacketData packetClass) {
        ProtocolState state = session.getProtocolState();
        if (state != ProtocolState.HANDSHAKE)
            return;

        session.setProtocolState(ProtocolState.LOGIN);
        session.sendPacket(new HandshakePacketData("-")); // skip online auth
    }
}