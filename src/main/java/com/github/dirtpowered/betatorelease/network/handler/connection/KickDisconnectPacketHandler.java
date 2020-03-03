package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.KickDisconnectPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import org.pmw.tinylog.Logger;

public class KickDisconnectPacketHandler implements BetaToModernHandler<KickDisconnectPacketData> {

    @Override
    public void handlePacket(Session session, KickDisconnectPacketData packetClass) {
        String reason = packetClass.getDisconnectReason();
        session.getModernClient().disconnect(reason);
        Logger.info("kick reason: {}", reason);
    }
}
