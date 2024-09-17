package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3MultiBlockChangePacketData;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;

public class ServerMultiBlockChangeTranslator implements ModernToBetaHandler<ServerMultiBlockChangePacket> {

    @Override
    public void translate(ServerMultiBlockChangePacket packet, Session betaSession) {
        BlockChangeRecord[] records = packet.getRecords();
        int size = records.length;

        byte[] blocks = new byte[size];
        byte[] data = new byte[size];
        short[] coordinates = new short[size];

        Position firstPos = records[0].getPosition();
        int chunkX = firstPos.getX() >> 4;
        int chunkZ = firstPos.getZ() >> 4;

        for (int i = 0; i < size; i++) {
            BlockChangeRecord record = records[i];

            BlockState blockState = record.getBlock();
            Position pos = record.getPosition();

            BlockMappings.RemappedBlock remap = BlockMappings.getRemappedBlock(blockState.getId(), blockState.getData());
            betaSession.getBlockStorage().setBlockAt(pos.getX(), pos.getY(), pos.getZ(), remap.blockId(), remap.blockData());

            int blockData = remap.blockData();
            if (Utils.isDoor(remap.blockId()))
                blockData = Utils.getLegacyDoorData(betaSession, pos.getX(), pos.getY(), pos.getZ(), blockData);

            blocks[i] = (byte) remap.blockId();
            data[i] = (byte) blockData;
            coordinates[i] = (short) ((pos.getX() & 0xF) << 12 | (pos.getZ() & 0xF) << 8 | pos.getY() & 0xFF);
        }

        betaSession.sendPacket(new V1_7_3MultiBlockChangePacketData(chunkX, chunkZ, coordinates, blocks, data, size));
    }
}