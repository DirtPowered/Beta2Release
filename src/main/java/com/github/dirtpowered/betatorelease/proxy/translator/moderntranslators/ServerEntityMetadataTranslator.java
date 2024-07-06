package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PickupSpawnPacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.data.entity.EntityItem;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.EntityMetadata;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.ItemStack;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.MetadataType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import org.pmw.tinylog.Logger;

public class ServerEntityMetadataTranslator implements ModernToBetaHandler<ServerEntityMetadataPacket> {

    @Override
    public void translate(ServerEntityMetadataPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        EntityMetadata[] metadata = packet.getMetadata();

        for (EntityMetadata entityMetadata : metadata) {
            //TODO: More entities
            //TODO: Send metadata to beta client too
            if (entityMetadata.getType() == MetadataType.ITEM) {
                EntityItem item = (EntityItem) betaSession.getServer().getEntityCache().getEntityById(entityId);
                if (item != null) {
                    if (entityMetadata.getValue() instanceof ItemStack) {
                        item.setItemStack(Utils.itemStackToBetaItemStack((ItemStack) entityMetadata.getValue()));
                        spawnItemEntity(betaSession, item);
                    }
                } else {
                    Logger.warn("Entity {} is not in EntityCache", entityId);
                }
            }
        }
    }

    private void spawnItemEntity(Session session, EntityItem itemEntity) {
        Location location = itemEntity.getLocation();

        session.sendPacket(new PickupSpawnPacketData(
                itemEntity.getEntityId(),
                (int) location.getX(),
                (int) location.getY(),
                (int) location.getZ(),
                (byte) itemEntity.getRotation(),
                (byte) itemEntity.getPitch(),
                (byte) itemEntity.getRoll(),
                itemEntity.getItemStack()
        ));

        session.getServer().getEntityCache().removeEntity(itemEntity.getEntityId());
    }
}
