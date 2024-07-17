package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.TransactionPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;

public class ServerConfirmTransactionTranslator implements ModernToBetaHandler<ServerConfirmTransactionPacket> {

    @Override
    public void translate(ServerConfirmTransactionPacket packet, Session betaSession) {
        int windowId = packet.getWindowId();
        int actionId = packet.getActionId();
        boolean isAccepted = packet.getAccepted();

        betaSession.sendPacket(new TransactionPacketData(windowId, actionId, isAccepted));
    }
}
