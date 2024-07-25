package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3CloseWindowPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCloseWindowPacket;

public class CloseWindowPacketHandler implements BetaToModernHandler<V1_7_3CloseWindowPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3CloseWindowPacketData packetClass) {
        int windowId = packetClass.getWindowId();

        session.getBetaPlayer().cacheWindowType(windowId, WindowType.GENERIC_INVENTORY); // set to default
        session.getModernClient().sendModernPacket(new ClientCloseWindowPacket(windowId));
    }
}