package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.MobSpawnPacketData;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.data.utils.OldMobType;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.google.common.collect.Lists;

public class ServerSpawnMobTranslator implements ModernToBetaHandler<ServerSpawnMobPacket> {

    @Override
    public void translate(ServerSpawnMobPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        byte type = (byte) OldMobType.fromMobType(packet.getType());
        if (type == -1)
            return;

        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());
        byte yaw = (byte) Utils.toAbsoluteRotation((int) packet.getYaw());
        byte pitch = (byte) Utils.toAbsoluteRotation((int) packet.getPitch());

        betaSession.getServer().getEntityCache().addEntity(new Entity(entityId, packet.getType()) {
            @Override
            public int getEntityId() {
                return super.getEntityId();
            }
        });
        betaSession.sendPacket(new MobSpawnPacketData(entityId, type, x, y, z, yaw, pitch, Lists.newArrayList()));
    }
}