package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.data.WatchableObject;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityMetadataPacketData;
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
import com.google.common.collect.Lists;

import java.util.List;

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
            } else {
                translateBaseEntityFlags(betaSession, entityId, entityMetadata);
                translateMobEntityFlags(betaSession, entity, entityMetadata);
            }
        }
    }

    private void translateBaseEntityFlags(Session session, int entityId, EntityMetadata metadata) {
        if (metadata.getType() != MetadataType.BYTE || metadata.getId() != 0)
            return;

        byte flags = (byte) metadata.getValue();
        List<WatchableObject> watchables = Lists.newArrayList();

        if ((flags & 0x02) == 0x02) // sneaking
            watchables.add(new WatchableObject(0, 0, (byte) 0x02));
        if ((flags & 0x01) == 0x01) // burning
            watchables.add(new WatchableObject(0, 0, (byte) 0x01));

        if (watchables.isEmpty()) {
            session.sendPacket(new V1_7_3EntityMetadataPacketData(entityId, List.of(new WatchableObject(0, 0, (byte) 0))));
        } else {
            session.sendPacket(new V1_7_3EntityMetadataPacketData(entityId, watchables));
        }
    }

    private void translateMobEntityFlags(Session session, Entity entity, EntityMetadata metadata) {
        if (entity == null || entity.getMobType() == null)
            return;

        int id = metadata.getId();
        MetadataType type = metadata.getType();
        Object value = metadata.getValue();

        if ((type != MetadataType.BYTE || id != 13) && (type != MetadataType.INT || id != 12) && (type != MetadataType.BOOLEAN || (id != 12 && id != 13)))
            return;

        if (type == MetadataType.BOOLEAN && id == 13)
            value = (boolean) value ? (byte) 0x01 : (byte) 0x00; // saddled pig

        session.sendPacket(new V1_7_3EntityMetadataPacketData(entity.getEntityId(), List.of(new WatchableObject(0, 16, value))));
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
    }
}