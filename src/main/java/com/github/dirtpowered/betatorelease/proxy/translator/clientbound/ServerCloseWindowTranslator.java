package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3CloseWindowPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;

public class ServerCloseWindowTranslator implements ModernToBetaHandler<ServerCloseWindowPacket> {

    @Override
    public void translate(ServerCloseWindowPacket packet, Session betaSession) {
        int windowId = packet.getWindowId();

        betaSession.getBetaPlayer().cacheWindowType(windowId, WindowType.GENERIC_INVENTORY); // set to default
        betaSession.sendPacket(new V1_7_3CloseWindowPacketData(windowId));
    }
}