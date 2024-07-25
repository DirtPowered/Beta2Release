package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3SetSlotPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;

public class ServerSetSlotTranslator implements ModernToBetaHandler<ServerSetSlotPacket> {

    @Override
    public void translate(ServerSetSlotPacket packet, Session betaSession) {
        int slot = packet.getSlot();
        WindowType windowType = betaSession.getBetaPlayer().getWindowType(packet.getWindowId());

        if (windowType == WindowType.GENERIC_INVENTORY && slot == 45)
            return; // skip offhand slot

        betaSession.sendPacket(new V1_7_3SetSlotPacketData(packet.getWindowId(), slot, Utils.itemStackToBetaItemStack(packet.getItem())));
    }
}