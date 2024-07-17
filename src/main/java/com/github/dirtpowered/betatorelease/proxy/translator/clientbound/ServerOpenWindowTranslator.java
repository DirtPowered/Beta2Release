package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.OpenWindowPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ServerOpenWindowTranslator implements ModernToBetaHandler<ServerOpenWindowPacket> {

    @Override
    public void translate(ServerOpenWindowPacket packet, Session betaSession) {
        int slotsCount = packet.getSlots();
        String windowTitle = TextComponent.toPlainText(ComponentSerializer.parse(packet.getName()));

        betaSession.getBetaPlayer().cacheWindowType(packet.getWindowId(), packet.getType());

        int inventoryType = switch (packet.getType()) {
            case CRAFTING_TABLE -> 1;
            case CHEST -> 0;
            case FURNACE -> 2;
            case DISPENSER -> 3;
            default -> -1;
        };

        if (inventoryType == -1)
            return;

        betaSession.sendPacket(new OpenWindowPacketData(packet.getWindowId(), inventoryType, windowTitle, slotsCount));
    }
}