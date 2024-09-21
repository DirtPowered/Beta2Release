package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3WindowItemsPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;

import java.util.Arrays;

public class ServerWindowItemsTranslator implements ModernToBetaHandler<ServerWindowItemsPacket> {

    @Override
    public void translate(ServerWindowItemsPacket packet, Session betaSession) {
        ItemStack[] items = packet.getItems();
        int windowId = packet.getWindowId();
        WindowType windowType = betaSession.getBetaPlayer().getWindowType(windowId);

        if (items.length == 46 /* skip offhand slot */ && (windowType == WindowType.GENERIC_INVENTORY
                || windowType == WindowType.DROPPER
                || windowType == WindowType.DISPENSER
                || windowType == WindowType.CRAFTING_TABLE)) {

            betaSession.sendPacket(new V1_7_3WindowItemsPacketData(windowId, Utils.convertItemStacks(Arrays.copyOf(items, 45))));
            return;
        }
        betaSession.sendPacket(new V1_7_3WindowItemsPacketData(windowId, Utils.convertItemStacks(items)));
    }
}