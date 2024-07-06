package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.data.BetaItemStack;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.SetSlotPacketData;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;

public class ServerSetSlotTranslator implements ModernToBetaHandler<ServerSetSlotPacket> {

    @Override
    public void translate(ServerSetSlotPacket packet, Session betaSession) {
        //TODO: Figure out why getting anything from ItemStack causing hanging on...
        //I found a really nice workaround...
        betaSession.getServer().getScheduledExecutorService().execute(() -> {
            int itemSlot = packet.getSlot();

            ItemStack itemStack = packet.getItem();
            if (itemStack == null && packet.getWindowId() != 255) {
                BetaItemStack item = new BetaItemStack(-1, 0, 0);
                betaSession.sendPacket(new SetSlotPacketData(0, itemSlot, item));
                return;
            }

            assert itemStack != null;
            int itemId = BlockMappings.getFixedItemId(itemStack.getId());
            int stackSize = itemStack.getAmount();
            int itemData = itemStack.getData();

            BetaItemStack item = new BetaItemStack(itemId, stackSize, itemData);
            betaSession.sendPacket(new SetSlotPacketData(0, itemSlot, item));
        });
    }
}
