package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3OpenWindowPacketData;
import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;

public class ServerOpenWindowTranslator implements ModernToBetaHandler<ServerOpenWindowPacket> {

    @Override
    public void translate(ServerOpenWindowPacket packet, Session betaSession) {
        int slotsCount = packet.getSlots();
        String windowTitle = LangStorage.translate(packet.getName(), true);

        betaSession.getBetaPlayer().cacheWindowType(packet.getWindowId(), packet.getType());

        int inventoryType = switch (packet.getType()) {
            case CRAFTING_TABLE -> 1;
            case CHEST, GENERIC_INVENTORY, SHULKER_BOX -> 0;
            case FURNACE -> 2;
            case DISPENSER, DROPPER -> 3;
            default -> -1;
        };

        if (inventoryType == -1)
            return;

        betaSession.sendPacket(new V1_7_3OpenWindowPacketData(packet.getWindowId(), inventoryType, windowTitle, slotsCount));
    }
}