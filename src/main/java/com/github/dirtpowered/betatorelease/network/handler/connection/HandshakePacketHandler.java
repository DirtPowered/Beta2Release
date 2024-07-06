package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.HandshakePacketData;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class HandshakePacketHandler implements BetaToModernHandler<HandshakePacketData> {

    @Override
    public void handlePacket(Session session, HandshakePacketData packetClass) {
        ProtocolState state = session.getProtocolState();
        if (state == ProtocolState.HANDSHAKE) {
            session.setProtocolState(ProtocolState.LOGIN);
            /* this.netManager.addToSendQueue(new Packet2Handshake("-")); */
            session.sendPacket(new HandshakePacketData("-")); //skip online auth
        }
    }
}