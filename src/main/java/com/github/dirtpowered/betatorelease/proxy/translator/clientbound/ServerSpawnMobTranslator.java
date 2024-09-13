package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3MobSpawnPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3NamedEntitySpawnPacketData;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.data.utils.OldMobType;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.google.common.collect.Lists;

public class ServerSpawnMobTranslator implements ModernToBetaHandler<ServerSpawnMobPacket> {
    private final static String NAME = "Villager";

    @Override
    public void translate(ServerSpawnMobPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        MobType mobType = packet.getType();

        byte type = OldMobType.fromMobType(mobType);
        if (type == -1 && mobType != MobType.VILLAGER)
            return;

        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());
        byte yaw = (byte) Utils.toAbsoluteRotation((int) packet.getYaw());
        byte pitch = (byte) Utils.toAbsoluteRotation((int) packet.getPitch());

        if (mobType == MobType.VILLAGER) {
            betaSession.sendPacket(new V1_7_3NamedEntitySpawnPacketData(entityId, NAME, x, y, z, yaw, pitch, 264));
            return;
        }
        betaSession.getEntityCache().addEntity(new Entity(entityId, mobType) {
            @Override
            public int getEntityId() {
                return super.getEntityId();
            }
        });
        betaSession.sendPacket(new V1_7_3MobSpawnPacketData(entityId, type, x, y, z, yaw, pitch, Lists.newArrayList()));
    }
}