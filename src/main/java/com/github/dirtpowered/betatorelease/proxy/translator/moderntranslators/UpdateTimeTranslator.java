package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UpdateTimePacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;

public class UpdateTimeTranslator implements ModernToBetaHandler<ServerUpdateTimePacket> {

    @Override
    public void translate(ServerUpdateTimePacket packet, Session betaSession) {
        long time = packet.getTime();
        betaSession.sendPacket(new UpdateTimePacketData(time));
    }
}