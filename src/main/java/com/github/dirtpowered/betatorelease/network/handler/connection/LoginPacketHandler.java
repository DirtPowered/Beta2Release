package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.LoginPacketData;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class LoginPacketHandler implements BetaToModernHandler<LoginPacketData> {

    @Override
    public void handlePacket(Session session, LoginPacketData packetClass) {
        ProtocolState protocolState = session.getProtocolState();

        if (protocolState == ProtocolState.LOGIN) {
            session.setProtocolState(ProtocolState.PLAY);
            session.setPlayerName(packetClass.getPlayerName());

            session.getModernClient().connect(); // connect to remote modern server
        }
    }
}