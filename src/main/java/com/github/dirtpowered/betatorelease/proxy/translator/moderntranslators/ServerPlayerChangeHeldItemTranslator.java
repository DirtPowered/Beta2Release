package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;

public class ServerPlayerChangeHeldItemTranslator implements ModernToBetaHandler<ServerPlayerChangeHeldItemPacket> {

    @Override
    public void translate(ServerPlayerChangeHeldItemPacket packet, Session betaSession) {
        int slot = packet.getSlot();
        //TODO: Is it possible?
    }
}
