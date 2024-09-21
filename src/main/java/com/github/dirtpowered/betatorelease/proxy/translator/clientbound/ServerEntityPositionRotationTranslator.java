package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityMoveLookPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.LegacyRelMovementUtil;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;

import java.util.List;

public class ServerEntityPositionRotationTranslator implements ModernToBetaHandler<ServerEntityPositionRotationPacket> {

    @Override
    public void translate(ServerEntityPositionRotationPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int x = Utils.toAbsolutePos(packet.getMovementX());
        int y = Utils.toAbsolutePos(packet.getMovementY());
        int z = Utils.toAbsolutePos(packet.getMovementZ());

        int yaw = Utils.toAbsoluteRotation(packet.getYaw());
        int pitch = Utils.toAbsoluteRotation(packet.getPitch());

        List<LegacyRelMovementUtil.RelPos> relPos = LegacyRelMovementUtil.toLegacyRelMove(x, y, z);

        for (LegacyRelMovementUtil.RelPos pos : relPos) {
            betaSession.sendPacket(new V1_7_3EntityMoveLookPacketData(entityId, pos.x(), pos.y(), pos.z(), yaw, pitch));
        }
    }
}