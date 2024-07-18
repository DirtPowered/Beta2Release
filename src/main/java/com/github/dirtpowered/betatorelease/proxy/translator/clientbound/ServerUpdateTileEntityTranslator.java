package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UpdateSignPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;

public class ServerUpdateTileEntityTranslator implements ModernToBetaHandler<ServerUpdateTileEntityPacket> {

    @Override
    public void translate(ServerUpdateTileEntityPacket packet, Session betaSession) {
        if (packet.getType() != UpdatedTileType.SIGN)
            return;

        Position pos = packet.getPosition();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        betaSession.sendPacket(new UpdateSignPacketData(x, y, z, Utils.getLegacySignLines(packet.getNBT())));
    }
}