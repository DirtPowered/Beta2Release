package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PickupSpawnPacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.data.entity.EntityItem;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;

public class ServerEntityMetadataTranslator implements ModernToBetaHandler<ServerEntityMetadataPacket> {

    @Override
    public void translate(ServerEntityMetadataPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        EntityMetadata[] metadata = packet.getMetadata();
        Entity entity = betaSession.getEntityCache().getEntityById(entityId);

        for (EntityMetadata entityMetadata : metadata) {
            if (entityMetadata.getType() == MetadataType.ITEM && entity instanceof EntityItem item) {
                if (entityMetadata.getValue() instanceof ItemStack) {
                    item.setItemStack(Utils.itemStackToBetaItemStack((ItemStack) entityMetadata.getValue()));
                    spawnItemEntity(betaSession, item);
                }
            }
        }
    }

    private void spawnItemEntity(Session session, EntityItem itemEntity) {
        Location location = itemEntity.getLocation();

        session.sendPacket(new V1_7_3PickupSpawnPacketData(
                itemEntity.getEntityId(),
                (int) location.getX(),
                (int) location.getY(),
                (int) location.getZ(),
                (byte) itemEntity.getRotation(),
                (byte) itemEntity.getPitch(),
                (byte) itemEntity.getRoll(),
                itemEntity.getItemStack()
        ));

        session.getEntityCache().removeEntity(itemEntity.getEntityId());
    }
}