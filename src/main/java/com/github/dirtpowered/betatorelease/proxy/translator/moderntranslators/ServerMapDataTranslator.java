package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.MapDataPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;

public class ServerMapDataTranslator implements ModernToBetaHandler<ServerMapDataPacket> {

    @Override
    public void translate(ServerMapDataPacket packet, Session betaSession) {
        // TODO: implement
        betaSession.sendPacket(new MapDataPacketData(packet.getMapId(), new byte[0]));
    }
}