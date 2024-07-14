package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BlockChangePacketData;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;

public class ServerBlockChangeTranslator implements ModernToBetaHandler<ServerBlockChangePacket> {

    @Override
    public void translate(ServerBlockChangePacket packet, Session betaSession) {
        BlockChangeRecord record = packet.getRecord();
        Position position = record.getPosition();
        BlockState block = record.getBlock();

        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        int blockId = block.getId();
        int blockData = block.getData();

        BlockMappings.RemappedBlock remap = BlockMappings.getRemappedBlock(blockId, blockData);
        betaSession.sendPacket(new BlockChangePacketData(x, y, z, remap.blockId(), remap.blockData()));
    }
}