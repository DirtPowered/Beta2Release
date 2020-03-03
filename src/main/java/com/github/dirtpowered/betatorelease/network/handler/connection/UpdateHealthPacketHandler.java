package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.UpdateHealthPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;

public class UpdateHealthPacketHandler implements BetaToModernHandler<UpdateHealthPacketData> {

    @Override
    public void handlePacket(Session session, UpdateHealthPacketData packetClass) {

    }
}
