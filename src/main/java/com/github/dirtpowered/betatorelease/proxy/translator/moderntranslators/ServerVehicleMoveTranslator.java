package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;

public class ServerVehicleMoveTranslator implements ModernToBetaHandler<ServerVehicleMovePacket> {

    @Override
    public void translate(ServerVehicleMovePacket packet, Session betaSession) {
        //TODO: vehicle steer
    }
}
