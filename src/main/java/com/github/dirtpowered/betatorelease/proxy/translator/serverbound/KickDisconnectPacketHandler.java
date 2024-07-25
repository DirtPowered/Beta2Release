package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3KickDisconnectPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import org.pmw.tinylog.Logger;

public class KickDisconnectPacketHandler implements BetaToModernHandler<V1_7_3KickDisconnectPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3KickDisconnectPacketData packetClass) {
        String reason = packetClass.getDisconnectReason();
        session.getModernClient().disconnect(reason);

        Logger.info("kick reason: {}", reason);
    }
}