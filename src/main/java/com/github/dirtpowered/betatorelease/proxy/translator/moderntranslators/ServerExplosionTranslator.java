package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.data.ExplosionPacketData;
import com.github.dirtpowered.betaprotocollib.utils.Location;
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
        float radius = packet.getRadius();
        List<Location> locations = packet.getExploded().stream().map(block -> new Location(block.getX(), block.getY(), block.getZ())).collect(Collectors.toList());

        betaSession.sendPacket(new ExplosionPacketData(x, y, z, radius, locations));
    }
}
