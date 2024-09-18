package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3LoginPacketData;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;

public class LoginPacketHandler implements BetaToModernHandler<V1_7_3LoginPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3LoginPacketData packetClass) {
        ProtocolState protocolState = session.getProtocolState();

        if (protocolState != ProtocolState.LOGIN)
            return;

        // limit client version to 1.7.3, if enabled
        if (session.getServer().getConfiguration().isStrictVersionCheck()) {
            if (packetClass.getEntityId() != MinecraftVersion.B_1_7_3.getProtocolVersion()) {
                session.disconnect("Invalid client version, please use 1.7.3");
                return;
            }
        }
        session.setProtocolState(ProtocolState.PLAY);
        session.setPlayerName(packetClass.getPlayerName());

        session.getModernClient().connect(); // connect to remote modern server
    }
}