package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.CloseWindowPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;

public class ServerCloseWindowTranslator implements ModernToBetaHandler<ServerCloseWindowPacket> {

    @Override
    public void translate(ServerCloseWindowPacket packet, Session betaSession) {
        int windowId = packet.getWindowId();

        betaSession.sendPacket(new CloseWindowPacketData(windowId));
    }
}
