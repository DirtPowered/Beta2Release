package com.github.dirtpowered.betatorelease.data.chunk;

import java.util.HashMap;
import java.util.Map;

public class BlockStorage {
    private final Map<Long, ChunkPart> blockStorage = new HashMap<>();

    private long getKey(int chunkX, int chunkZ) {
        return ((long) chunkX & 0xffffffffL) | (((long) chunkZ & 0xffffffffL) << 32);
    }

    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return blockStorage.get(getKey(chunkX, chunkZ)) != null;
    }

    public void removeChunk(int chunkX, int chunkZ) {
        this.blockStorage.remove(getKey(chunkX, chunkZ));
    }

    public Block getBlockAt(int x, int y, int z) {
        ChunkPart part = blockStorage.get(getKey(x >> 4, z >> 4));
        if (part == null)
            return new Block(x, y, z, 0, 0);

        int blockId = part.getBlock(x & 15, y & 255, z & 15);
        int blockData = part.getBlockData(x & 15, y & 255, z & 15);

        return new Block(x, y, z, blockId, blockData);
    }

    public void setBlockAt(int x, int y, int z, int blockId, int blockData) {
        ChunkPart part = blockStorage.computeIfAbsent(getKey(x >> 4, z >> 4), k -> new ChunkPart());
        part.setBlock(x & 15, y & 255, z & 15, blockId, blockData);
    }

    private static class ChunkPart {
        private static final int SIZE_X = 16;
        private static final int SIZE_Y = 256;
        private static final int SIZE_Z = 16;

        private static final int BLOCKS_SIZE = SIZE_X * SIZE_Y * SIZE_Z;
        private final short[] blocks = new short[BLOCKS_SIZE];

        int getBlock(int posX, int posY, int posZ) {
            if (isValidPosition(posX, posY, posZ)) {
                return (blocks[getIndex(posX, posY, posZ)] >> 4) & 0xFFF;
            }
            return 0;
        }

        int getBlockData(int posX, int posY, int posZ) {
            if (isValidPosition(posX, posY, posZ)) {
                return blocks[getIndex(posX, posY, posZ)] & 0xF;
            }
            return 0;
        }

        void setBlock(int posX, int posY, int posZ, int blockId, int blockData) {
            if (isValidPosition(posX, posY, posZ)) {
                this.blocks[getIndex(posX, posY, posZ)] = (short) ((blockId << 4) | (blockData & 0xF));
            }
        }

        private int getIndex(int posX, int posY, int posZ) {
            return (posY * SIZE_Z + posZ) * SIZE_X + posX;
        }

        private boolean isValidPosition(int posX, int posY, int posZ) {
            return posX >= 0 && posY >= 0 && posZ >= 0 && posX < SIZE_X && posY < SIZE_Y && posZ < SIZE_Z;
        }
    }
}