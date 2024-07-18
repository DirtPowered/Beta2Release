package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.WindowItemsPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;

public class ServerWindowItemsTranslator implements ModernToBetaHandler<ServerWindowItemsPacket> {

    @Override
    public void translate(ServerWindowItemsPacket packet, Session betaSession) {
        ItemStack[] items = packet.getItems();
        int windowId = packet.getWindowId();
        WindowType windowType = betaSession.getBetaPlayer().getWindowType(windowId);

        if (items.length == 46 && windowType == WindowType.GENERIC_INVENTORY) { // skip offhand slot
            ItemStack[] newStacks = new ItemStack[45];

            System.arraycopy(items, 0, newStacks, 0, 44);
            betaSession.sendPacket(new WindowItemsPacketData(windowId, Utils.convertItemStacks(newStacks)));
            return;
        }
        betaSession.sendPacket(new WindowItemsPacketData(windowId, Utils.convertItemStacks(items)));
    }
}