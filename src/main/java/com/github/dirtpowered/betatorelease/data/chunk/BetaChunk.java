package com.github.dirtpowered.betatorelease.data.chunk;

import lombok.Getter;

/**
 * Represents a chunk of the map.
 *
 * @author Graham Edgecombe
 */
public class BetaChunk {

    /**
     * The dimensions of a chunk.
     */
    private static final int WIDTH = 16, HEIGHT = 16, DEPTH = 128;

    /**
     * The coordinates of this chunk.
     */
    @Getter
    private final int x, z;

    /**
     * The data in this chunk representing all the blocks and their state.
     */
    private final byte[] types, metaData, skyLight, blockLight;

    /**
     * Creates a new chunk with a specified X and Z coordinate.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     */
    public BetaChunk(int x, int z) {
        this.x = x;
        this.z = z;
        this.types = new byte[WIDTH * HEIGHT * DEPTH];
        this.metaData = new byte[WIDTH * HEIGHT * DEPTH];
        this.skyLight = new byte[WIDTH * HEIGHT * DEPTH];
        this.blockLight = new byte[WIDTH * HEIGHT * DEPTH];
    }

    public void setBlock(final int x, final int y, final int z, final int type) {
        if (type < 0 || type >= 97)
            throw new IllegalArgumentException();

        this.types[getIndex(x, y, z)] = (byte) type;
    }

    /**
     * Sets the metadata of a block within this chunk.
     *
     * @param x        The X coordinate.
     * @param z        The Z coordinate.
     * @param y        The Y coordinate.
     * @param metaData The metadata.
     */
    public void setMetaData(int x, int y, int z, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException();

        this.metaData[getIndex(x, y, z)] = (byte) metaData;
    }

    private int getIndex(final int x, final int y, final int z) {
        return (Math.min(16, x) * 16 + Math.min(16, z)) * 128 + Math.min(128, y);
    }

    /**
     * Sets the skylight level of a block within this chunk.
     *
     * @param x        The X coordinate.
     * @param z        The Z coordinate.
     * @param y        The Y coordinate.
     * @param skyLight The skylight level.
     */
    public void setSkyLight(int x, int y, int z, int skyLight) {
        if (skyLight < 0 || skyLight >= 16)
            throw new IllegalArgumentException();

        this.skyLight[getIndex(x, y, z)] = (byte) skyLight;
    }

    /**
     * Sets the block light level of a block within this chunk.
     *
     * @param x          The X coordinate.
     * @param z          The Z coordinate.
     * @param y          The Y coordinate.
     * @param blockLight The block light level.
     */
    public void setBlockLight(int x, int y, int z, int blockLight) {
        if (blockLight < 0 || blockLight >= 16)
            throw new IllegalArgumentException();

        this.blockLight[getIndex(x, y, z)] = (byte) blockLight;
    }

    /**
     * Serializes tile data into a byte array.
     *
     * @return The byte array populated with the tile data.
     */
    public byte[] serializeTileData() {
        byte[] data = new byte[((WIDTH * HEIGHT * DEPTH * 5) / 2)];

        System.arraycopy(types, 0, data, 0, types.length);

        int pos = types.length;

        for (int i = 0; i < metaData.length; i += 2) {
            byte meta1 = metaData[i];
            byte meta2 = metaData[i + 1];
            data[pos++] = (byte) ((meta2 << 4) | meta1);
        }

        for (int i = 0; i < blockLight.length; i += 2) {
            byte light1 = blockLight[i];
            byte light2 = blockLight[i + 1];
            data[pos++] = (byte) ((light2 << 4) | light1);
        }

        for (int i = 0; i < skyLight.length; i += 2) {
            byte light1 = skyLight[i];
            byte light2 = skyLight[i + 1];
            data[pos++] = (byte) ((light2 << 4) | light1);
        }

        return data;
    }
}