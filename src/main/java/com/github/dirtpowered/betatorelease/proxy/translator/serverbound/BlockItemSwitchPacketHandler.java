package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BlockItemSwitchPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerChangeHeldItemPacket;

public class BlockItemSwitchPacketHandler implements BetaToModernHandler<BlockItemSwitchPacketData> {

    @Override
    public void handlePacket(Session session, BlockItemSwitchPacketData packetClass) {
        session.getModernClient().sendModernPacket(new ClientPlayerChangeHeldItemPacket(packetClass.getSlot()));
    }
}