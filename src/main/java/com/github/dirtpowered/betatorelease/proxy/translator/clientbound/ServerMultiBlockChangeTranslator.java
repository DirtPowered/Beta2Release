package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3BlockChangePacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3MultiBlockChangePacketData;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.data.chunk.Block;
import com.github.dirtpowered.betatorelease.data.chunk.BlockStorage;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.LegacyDoorDataFixer;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;

import java.util.HashSet;
import java.util.Set;

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

        // Check if chunk is loaded. Beta 1.7.3 client will crash if block is changed in unloaded chunk
        BlockStorage storage = betaSession.getBlockStorage();
        if (!storage.isChunkLoaded(chunkX, chunkZ)) {
            Main.LOGGER.warn("Attempted to change blocks at unloaded chunk: " + firstPos.getX() + " " + firstPos.getY() + " " + firstPos.getZ());
            return;
        }

        Set<Block> doors = new HashSet<>();

        for (int i = 0; i < size; i++) {
            BlockChangeRecord record = records[i];

            BlockState blockState = record.getBlock();
            Position pos = record.getPosition();

            BlockMappings.RemappedBlock remap = BlockMappings.getRemappedBlock(blockState.getId(), blockState.getData());
            storage.setBlockAt(pos.getX(), pos.getY(), pos.getZ(), remap.blockId(), remap.blockData());

            if (Utils.isDoor(remap.blockId())) {
                doors.add(new Block(pos.getX(), pos.getY(), pos.getZ(), remap.blockId(), remap.blockData()));
                continue;
            }

            int blockData = remap.blockData();

            blocks[i] = (byte) remap.blockId();
            data[i] = (byte) blockData;
            coordinates[i] = (short) ((pos.getX() & 0xF) << 12 | (pos.getZ() & 0xF) << 8 | pos.getY() & 0xFF);
        }

        /* Update doors
         * Same as in ServerChunkDataTranslator,
         * We need 2 halves of the door to calculate the correct data
         */
        for (Block block : doors) {
            int x = block.getX();
            int y = block.getY();
            int z = block.getZ();

            int fixedData = LegacyDoorDataFixer.getLegacyDoorData(betaSession, x, y, z, block.getBlockData());
            betaSession.sendPacket(new V1_7_3BlockChangePacketData(x, y, z, block.getBlockId(), fixedData));
        }
        betaSession.sendPacket(new V1_7_3MultiBlockChangePacketData(chunkX, chunkZ, coordinates, blocks, data, size));
    }
}