package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.EntityEquipmentPacketData;
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

        int betaSlot;

        switch (slot) {
            case MAIN_HAND:
                betaSlot = 0;
                break;
            case OFF_HAND:
                betaSlot = -1;
                break;
            case BOOTS:
                betaSlot = 1;
                break;
            case LEGGINGS:
                betaSlot = 2;
                break;
            case CHESTPLATE:
                betaSlot = 3;
                break;
            case HELMET:
                betaSlot = 4;
                break;
            default:
                betaSlot = -1;
                break;
        }

        betaSession.sendPacket(new EntityEquipmentPacketData(entityId, betaSlot, itemId, itemData));
    }
}
