package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.NamedEntitySpawnPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.data.entity.EntityPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;

public class ServerSpawnPlayerTranslator implements ModernToBetaHandler<ServerSpawnPlayerPacket> {

    @Override
    public void translate(ServerSpawnPlayerPacket packet, Session betaSession) {
        //Spawning other players
        int entityId = packet.getEntityId();
        EntityPlayer player = betaSession.getServer().getPlayerCache().getPlayerFromUUID(packet.getUUID());
        String username;

        if (player == null) username = "null";
        else username = player.getUsername();

        int x = Utils.toAbsolutePos(packet.getX());
        int y = Utils.toAbsolutePos(packet.getY());
        int z = Utils.toAbsolutePos(packet.getZ());
        byte yaw = (byte) Utils.toAbsoluteRotation((int) packet.getYaw());
        byte pitch = (byte) Utils.toAbsoluteRotation((int) packet.getPitch());

        betaSession.sendPacket(new NamedEntitySpawnPacketData(entityId, username, x, y, z, yaw, pitch, 0));
    }
}
