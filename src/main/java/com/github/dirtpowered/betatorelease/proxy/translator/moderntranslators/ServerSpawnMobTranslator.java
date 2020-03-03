package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.MobSpawnPacketData;
import com.github.dirtpowered.betatorelease.Utils.Utils;
import com.github.dirtpowered.betatorelease.data.utils.OldMobType;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import org.pmw.tinylog.Logger;

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

        if (betaSession.getServer().isDebugMode()) {
            Logger.info("EntityType(id: {}): {} at x:{} y:{} z:{}", entityId, packet.getType(), x, y, z);
        }
        betaSession.sendPacket(new MobSpawnPacketData(entityId, type, x, y, z, yaw, pitch));
    }
}
