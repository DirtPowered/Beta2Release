package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b18;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.CreativeItemGetPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.client.window.ClientCreativeInventoryActionPacket;

public class CreativeItemGetPacketHandler implements BetaToModernHandler<CreativeItemGetPacketData> {

    @Override
    public void handlePacket(Session session, CreativeItemGetPacketData packetClass) {
        ItemStack itemStack = Utils.betaItemStackToItemStack(packetClass.getItemStack());
        session.getModernClient().sendModernPacket(new ClientCreativeInventoryActionPacket(packetClass.getSlotId(), itemStack));
    }
}