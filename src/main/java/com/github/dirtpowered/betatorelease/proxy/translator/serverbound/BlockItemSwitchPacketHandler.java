package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3BlockItemSwitchPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;

public class BlockItemSwitchPacketHandler implements BetaToModernHandler<V1_7_3BlockItemSwitchPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3BlockItemSwitchPacketData packetClass) {
        session.getModernClient().sendModernPacket(new ClientPlayerChangeHeldItemPacket(packetClass.getSlot()));
    }
}