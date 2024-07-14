package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BlockChangePacketData;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;

public class ServerMultiBlockChangeTranslator implements ModernToBetaHandler<ServerMultiBlockChangePacket> {

    @Override
    public void translate(ServerMultiBlockChangePacket packet, Session betaSession) {
        BlockChangeRecord[] records = packet.getRecords();
        for (BlockChangeRecord record : records) {
            BlockState blockState = record.getBlock();
            Position pos = record.getPosition();

            BlockMappings.RemappedBlock remap = BlockMappings.getRemappedBlock(blockState.getId(), blockState.getData());
            betaSession.sendPacket(new BlockChangePacketData(pos.getX(), pos.getY(), pos.getZ(), remap.blockId(), remap.blockData()));
        }
    }
}