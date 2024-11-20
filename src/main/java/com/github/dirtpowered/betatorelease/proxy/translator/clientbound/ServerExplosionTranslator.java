package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityVelocityPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ExplosionPacketData;
import com.github.dirtpowered.betaprotocollib.utils.BlockLocation;
import com.github.dirtpowered.betaprotocollib.utils.Location;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;

import java.util.List;
import java.util.stream.Collectors;

public class ServerExplosionTranslator implements ModernToBetaHandler<ServerExplosionPacket> {

    @Override
    public void translate(ServerExplosionPacket packet, Session betaSession) {
        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();

        List<BlockLocation> locations = packet.getExploded().stream().map(block -> new BlockLocation(block.getX(), block.getY(), block.getZ())).collect(Collectors.toList());
        betaSession.sendPacket(new V1_7_3ExplosionPacketData(x, y, z, packet.getRadius(), locations));

        // Apply additional velocity to the player.
        // Not sure if this is needed, server already sends velocity packets separately during the explosion.
        BetaPlayer player = betaSession.getBetaPlayer();

        float radius = packet.getRadius();

        // check if player is in explosion radius
        if (!(player.getLocation().distanceTo(new Location(x, y, z, 0, 0)) <= radius))
            return;

        int entityId = player.getEntityId();

        int motionX = Utils.toBetaVelocity(packet.getPushX());
        int motionY = Utils.toBetaVelocity(packet.getPushY());
        int motionZ = Utils.toBetaVelocity(packet.getPushZ());

        betaSession.sendPacket(new V1_7_3EntityVelocityPacketData(entityId, motionX, motionY, motionZ));
    }
}