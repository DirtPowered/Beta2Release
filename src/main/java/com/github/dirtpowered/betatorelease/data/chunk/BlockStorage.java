package com.github.dirtpowered.betatorelease.data.chunk;

import com.github.dirtpowered.betatorelease.utils.Utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockStorage {
    private final Map<Long, ChunkPart> blockStorage = new ConcurrentHashMap<>();

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

        return part.getBlock(x & 15, y & 255, z & 15);
    }

    public void setBlockAt(int x, int y, int z, int blockId, int blockData) {
        if (!Utils.isAllowedCacheBlock(blockId))
            return;

        ChunkPart part = blockStorage.computeIfAbsent(getKey(x >> 4, z >> 4), k -> new ChunkPart());
        part.setBlock(x & 15, y & 255, z & 15, blockId, blockData);
    }

    private static class ChunkPart {
        private static final int SIZE_X = 16;
        private static final int SIZE_Y = 128;
        private static final int SIZE_Z = 16;
        private static final int TOTAL_BLOCKS = SIZE_X * SIZE_Y * SIZE_Z;

        private final int[] blockIds = new int[TOTAL_BLOCKS];
        private final byte[] blockData = new byte[TOTAL_BLOCKS / 2];

        public Block getBlock(int posX, int posY, int posZ) {
            int index = getIndex(posX, posY, posZ);
            return new Block(posX, posY, posZ, blockIds[index], getNibble(index));
        }

        public void setBlock(int posX, int posY, int posZ, int blockId, int blockData) {
            int index = getIndex(posX, posY, posZ);
            this.blockIds[index] = blockId;
            setNibble(index, blockData);
        }

        private int getIndex(int posX, int posY, int posZ) {
            return (posY * SIZE_Z + posZ) * SIZE_X + posX;
        }

        private void setNibble(int index, int value) {
            int nIndex = index / 2;
            if ((index % 2) == 0) {
                this.blockData[nIndex] = (byte) ((blockData[nIndex] & 0x0F) | ((value & 0xF) << 4));
            } else {
                this.blockData[nIndex] = (byte) ((blockData[nIndex] & 0xF0) | (value & 0xF));
            }
        }

        private int getNibble(int index) {
            int nIndex = index / 2;
            if ((index % 2) == 0) {
                return (blockData[nIndex] >> 4) & 0xF;
            } else {
                return blockData[nIndex] & 0xF;
            }
        }
    }
}