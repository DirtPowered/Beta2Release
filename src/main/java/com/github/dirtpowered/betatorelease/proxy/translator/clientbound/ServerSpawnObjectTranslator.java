package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityTeleportPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3VehicleSpawnPacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.data.entity.EntityItem;
import com.github.dirtpowered.betatorelease.data.entity.EntityVehicle;
import com.github.dirtpowered.betatorelease.data.entity.cache.EntityCache;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.FallingBlockData;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.MinecartType;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.ObjectType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;

public class ServerSpawnObjectTranslator implements ModernToBetaHandler<ServerSpawnObjectPacket> {

    @Override
    public void translate(ServerSpawnObjectPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int viewer = betaSession.getBetaPlayer().getEntityId();

        EntityCache entityCache = betaSession.getEntityCache();

        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());

        int velocityX = Utils.toBetaVelocity(packet.getMotionX());
        int velocityY = Utils.toBetaVelocity(packet.getMotionY());
        int velocityZ = Utils.toBetaVelocity(packet.getMotionZ());

        switch (packet.getType()) {
            case ITEM, ITEM_FRAME:
                float yaw = ((int) (packet.getMotionX() * 128.0D));
                float pitch = ((int) (packet.getMotionY() * 128.0D));
                float roll = ((int) (packet.getMotionZ() * 128.0D));

                EntityItem entityItem = new EntityItem(entityId);
                entityItem.setLocation(new Location(x, y, z, 0, 0));

                handleItemFrames(packet, betaSession, entityItem, entityId);
                entityItem.setRotation(yaw);
                entityItem.setPitch(pitch);
                entityItem.setRoll(roll);

                // set initial velocity
                entityItem.setVelocityX(velocityX);
                entityItem.setVelocityY(velocityY);
                entityItem.setVelocityZ(velocityZ);

                entityCache.addEntity(entityItem);
                break;
            case TIPPED_ARROW:
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, 60, x, y, z, velocityX, velocityY, velocityZ, viewer));
                break;
            case PRIMED_TNT:
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, 50, x, y, z, velocityX, velocityY, velocityZ, 0));
                break;
            case SNOWBALL, ENDER_PEARL, WITHER_HEAD_PROJECTILE:
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, 61, x, y, z, velocityX, velocityY, velocityZ, 0));
                break;
            case EGG, POTION, EXP_BOTTLE:
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, 62, x, y, z, velocityX, velocityY, velocityZ, 0));
                break;
            case FISH_HOOK:
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, 90, x, y, z, velocityX, velocityY, velocityZ, 0));
                break;
            case BOAT:
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, 1, x, y, z, velocityX, velocityY, velocityZ, 0));
                entityCache.addEntity(new EntityVehicle(entityId, true));
                break;
            case GHAST_FIREBALL, BLAZE_FIREBALL, DRAGON_FIREBALL:
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, 63, x, y, z, velocityX, velocityY, velocityZ, 0));
                break;
            case MINECART:
                int typeId = packet.getData() instanceof MinecartType minecartType ? switch (minecartType) {
                    case POWERED -> 12;
                    case CHEST -> 11;
                    default -> 10;
                } : 10;

                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, typeId, x, y, z, velocityX, velocityY, velocityZ, 0));
                entityCache.addEntity(new EntityVehicle(entityId, false));
                break;
            case FALLING_BLOCK:
                if (!(packet.getData() instanceof FallingBlockData data))
                    return;

                int entityType = data.getId() == 13 ? 71 : 70;
                betaSession.sendPacket(new V1_7_3VehicleSpawnPacketData(entityId, entityType, x, y, z, velocityX, velocityY, velocityZ, 0));
                break;
        }
    }

    private static void handleItemFrames(ServerSpawnObjectPacket packet, Session betaSession, EntityItem entityItem, int entityId) {
        if (packet.getType() != ObjectType.ITEM_FRAME)
            return;

        final int cx = Utils.toAbsolutePos(packet.getX() + 0.5D);
        final int cy = Utils.toAbsolutePos(packet.getY() + 0.5D);
        final int cz = Utils.toAbsolutePos(packet.getZ() + 0.5D);

        entityItem.setTickable(true);
        entityItem.setLocation(new Location(cx, cy, cz, 0, 0));

        entityItem.setTickConsumer(e -> {
            // teleport to original location
            betaSession.sendPacket(new V1_7_3EntityTeleportPacketData(entityId, cx, cy, cz, 0, 0));
        });
    }
}