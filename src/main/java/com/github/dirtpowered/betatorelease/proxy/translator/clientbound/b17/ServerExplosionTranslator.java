package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.ExplosionPacketData;
import com.github.dirtpowered.betaprotocollib.utils.BlockLocation;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;

import java.util.List;
import java.util.stream.Collectors;

public class ServerExplosionTranslator implements ModernToBetaHandler<ServerExplosionPacket> {

    @Override
    public void translate(ServerExplosionPacket packet, Session betaSession) {
        int x = (int) packet.getX();
        int y = (int) packet.getY();
        int z = (int) packet.getZ();

        List<BlockLocation> locations = packet.getExploded().stream().map(block -> new BlockLocation(block.getX(), block.getY(), block.getZ())).collect(Collectors.toList());
        betaSession.sendPacket(new ExplosionPacketData(x, y, z, packet.getRadius(), locations));
    }
}