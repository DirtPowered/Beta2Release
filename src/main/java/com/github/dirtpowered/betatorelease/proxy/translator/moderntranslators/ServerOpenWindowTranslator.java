package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.OpenWindowPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;

public class ServerOpenWindowTranslator implements ModernToBetaHandler<ServerOpenWindowPacket> {

    @Override
    public void translate(ServerOpenWindowPacket packet, Session betaSession) {
        int slotsCount = packet.getSlots();
        String windowTitle = packet.getName();

        int inventoryType;

        switch (packet.getType()) {
            case CRAFTING_TABLE:
                inventoryType = 1;
                break;
            case CHEST:
                inventoryType = 0;
                break;
            case FURNACE:
                inventoryType = 2;
                break;
            case DISPENSER:
                inventoryType = 3;
                break;
            default:
                inventoryType = -1;
                break;
        }

        if (inventoryType == -1)
            return;

        betaSession.sendPacket(new OpenWindowPacketData(0/* always 0 */, inventoryType, windowTitle, slotsCount));
    }
}
