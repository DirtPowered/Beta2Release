package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.CloseWindowPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;

public class CloseWindowPacketHandler implements BetaToModernHandler<CloseWindowPacketData> {

    @Override
    public void handlePacket(Session session, CloseWindowPacketData packetClass) {
        int windowId = packetClass.getWindowId();
        session.getModernClient().sendModernPacket(new ClientCloseWindowPacket(windowId));
    }
}
