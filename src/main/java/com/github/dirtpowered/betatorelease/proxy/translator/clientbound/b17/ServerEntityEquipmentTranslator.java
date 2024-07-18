package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityEquipmentPacketData;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.EquipmentSlot;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;

public class ServerEntityEquipmentTranslator implements ModernToBetaHandler<ServerEntityEquipmentPacket> {

    @Override
    public void translate(ServerEntityEquipmentPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        EquipmentSlot slot = packet.getSlot();
        ItemStack itemStack = packet.getItem();

        if (itemStack == null)
            itemStack = new ItemStack(-1);

        int itemId = itemStack.getId();
        int itemData = itemStack.getData();

        int betaSlot = switch (slot) {
            case MAIN_HAND -> 0;
            case BOOTS -> 1;
            case LEGGINGS -> 2;
            case CHESTPLATE -> 3;
            case HELMET -> 4;
            default -> -1;
        };

        if (betaSlot == -1)
            return;

        BlockMappings.RemappedItem remap = BlockMappings.getRemappedItem(itemId, itemData);
        betaSession.sendPacket(new EntityEquipmentPacketData(entityId, betaSlot, remap.itemId(), remap.itemData()));
    }
}