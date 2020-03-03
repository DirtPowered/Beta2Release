package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.WindowItemsPacketData;
import com.github.dirtpowered.betatorelease.Utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;

public class ServerWindowItemsTranslator implements ModernToBetaHandler<ServerWindowItemsPacket> {

    @Override
    public void translate(ServerWindowItemsPacket packet, Session betaSession) {
        betaSession.getServer().getScheduledExecutorService().execute(() -> {
            ItemStack[] stacks = packet.getItems();
            int windowId = packet.getWindowId();
            betaSession.sendPacket(new WindowItemsPacketData(windowId, Utils.convertItemStacks(stacks)));
        });
    }
}
