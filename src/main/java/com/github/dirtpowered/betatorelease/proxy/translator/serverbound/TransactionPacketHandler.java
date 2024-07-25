package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3TransactionPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientConfirmTransactionPacket;

public class TransactionPacketHandler implements BetaToModernHandler<V1_7_3TransactionPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3TransactionPacketData packetClass) {
        int windowId = packetClass.getWindowId();
        int shortWindowId = packetClass.getShortWindowId();
        boolean accepted = packetClass.isAccepted();

        session.getModernClient().sendModernPacket(new ClientConfirmTransactionPacket(windowId, shortWindowId, accepted));
    }
}