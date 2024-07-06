package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.VehicleSpawnPacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.data.entity.EntityItem;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.type.object.FallingBlockData;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;

public class ServerSpawnObjectTranslator implements ModernToBetaHandler<ServerSpawnObjectPacket> {

    @Override
    public void translate(ServerSpawnObjectPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());

        int velocityX = Utils.toBetaVelocity(packet.getMotionX());
        int velocityY = Utils.toBetaVelocity(packet.getMotionY());
        int velocityZ = Utils.toBetaVelocity(packet.getMotionZ());

        switch (packet.getType()) {
            case ITEM:
                float yaw = ((int) (packet.getMotionX() * 128.0D));
                float pitch = ((int) (packet.getMotionY() * 128.0D));
                float roll = ((int) (packet.getMotionZ() * 128.0D));

                EntityItem entityItem = new EntityItem(entityId);
                entityItem.setLocation(new Location(x, y, z, 0, 0));

                entityItem.setRotation(yaw);
                entityItem.setPitch(pitch);
                entityItem.setRoll(roll);

                betaSession.getServer().getEntityCache().addEntity(entityItem);
                break;
            case TIPPED_ARROW:
                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, 60, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
            case PRIMED_TNT:
                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, 50, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
            case SNOWBALL:
                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, 61, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
            case EGG:
                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, 62, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
            case FISH_HOOK:
                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, 90, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
            case BOAT:
                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, 1, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
            case GHAST_FIREBALL:
                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, 63, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
            case FALLING_BLOCK:
                FallingBlockData data = (FallingBlockData) packet.getData();
                int entityType = data.getId() == 13 ? 71 : 70;

                betaSession.sendPacket(new VehicleSpawnPacketData(entityId, entityType, x, y, z, 0, velocityX, velocityY, velocityZ));
                break;
        }
    }
}