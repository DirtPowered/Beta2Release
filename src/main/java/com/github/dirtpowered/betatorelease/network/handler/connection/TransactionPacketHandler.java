package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.TransactionPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;

public class TransactionPacketHandler implements BetaToModernHandler<TransactionPacketData> {

    @Override
    public void handlePacket(Session session, TransactionPacketData packetClass) {
        int windowId = packetClass.getWindowId();
        int shortWindowId = packetClass.getShortWindowId();
        boolean accepted = packetClass.isAccepted();

        session.getModernClient().sendModernPacket(new ClientConfirmTransactionPacket(windowId, shortWindowId, accepted));
    }
}