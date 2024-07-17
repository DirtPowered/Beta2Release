package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.MapChunkPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PreChunkPacketData;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.data.chunk.BetaChunk;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.pmw.tinylog.Logger;

public class ServerChunkDataTranslator implements ModernToBetaHandler<ServerChunkDataPacket> {

    @Override
    public void translate(ServerChunkDataPacket packet, Session betaSession) {
        Column chunkColumn = packet.getColumn();

        int xPosition = chunkColumn.getX();
        int zPosition = chunkColumn.getZ();

        BetaChunk betaChunk = new BetaChunk(xPosition, zPosition);
        boolean fullChunk = chunkColumn.hasBiomeData();

        //https://wiki.vg/index.php?title=Protocol&oldid=689#Pre-Chunk_.280x32.29
        if (fullChunk)
            betaSession.sendPacket(new PreChunkPacketData(xPosition, zPosition, true /* allocate space */));

        Chunk[] chunks = chunkColumn.getChunks();

        try {
            for (int index = 0; index < chunks.length; index++) {
                if (index >= 8) // we don't need to send chunks above 128 since beta doesn't support them
                    break;

                Chunk chunk = chunks[index];
                if (chunk == null)
                    continue;

                final int columnCurrentHeight = index * 16; //(0-127)

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            BlockState blockState = chunk.getBlocks().get(x, y, z);
                            BlockMappings.RemappedBlock remap = BlockMappings.getRemappedBlock(blockState.getId(), blockState.getData());

                            betaChunk.setBlock(x, y + columnCurrentHeight, z, remap.blockId());
                            betaChunk.setMetaData(x, y + columnCurrentHeight, z, remap.blockData());
                            betaChunk.setBlockLight(x, y + columnCurrentHeight, z, chunk.getBlockLight().get(x, y, z));

                            if (chunkColumn.hasSkylight()) { // there's no skylight in end/nether
                                betaChunk.setSkyLight(x, y + columnCurrentHeight, z, chunk.getSkyLight().get(x, y, z));
                            }
                        }
                    }
                }
            }

            betaSession.sendPacket(new MapChunkPacketData(betaChunk.getX() * 16, (short) 0, betaChunk.getZ() * 16, 16, 128, 16, betaChunk.serializeTileData()));
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Chunk error: {}", e);
        }
    }
}