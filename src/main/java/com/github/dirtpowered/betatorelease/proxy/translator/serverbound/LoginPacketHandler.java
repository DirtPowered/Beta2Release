package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3LoginPacketData;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class LoginPacketHandler implements BetaToModernHandler<V1_7_3LoginPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3LoginPacketData packetClass) {
        ProtocolState protocolState = session.getProtocolState();

        if (protocolState != ProtocolState.LOGIN)
            return;

        session.setProtocolState(ProtocolState.PLAY);
        session.setPlayerName(packetClass.getPlayerName());

        session.getModernClient().connect(); // connect to remote modern server
    }
}