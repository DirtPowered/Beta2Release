package com.github.dirtpowered.betatorelease.data.chunk;

import java.util.Arrays;

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
    private final int x, z;

    /**
     * The data in this chunk representing all of the blocks and their state.
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

        Arrays.fill(blockLight, (byte) 15); //fill array, cuz updating blocklight is slow
    }

    /**
     * Gets the X coordinate of this chunk.
     *
     * @return The X coordinate of this chunk.
     */
    public int getX() {
        return x;
    }

    /**
     * Gets the Z coordinate of this chunk.
     *
     * @return The Z coordinate of this chunk.
     */
    public int getZ() {
        return z;
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
    public void setMetaData(int x, int z, int y, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException();

        this.metaData[getIndex(x, z, y)] = (byte) metaData;
    }

    private int getIndex(final int x, final int y, final int z) {
        return (Math.min(16, x) * 16 + Math.min(16, z)) * 128 + Math.min(128, y);
    }

    /**
     * Sets the sky light level of a block within this chunk.
     *
     * @param x        The X coordinate.
     * @param z        The Z coordinate.
     * @param y        The Y coordinate.
     * @param skyLight The sky light level.
     */
    public void setSkyLight(int x, int z, int y, int skyLight) {
        if (skyLight < 0 || skyLight >= 16)
            throw new IllegalArgumentException();

        this.skyLight[getIndex(x, z, y)] = (byte) skyLight;
    }

    /**
     * Sets the block light level of a block within this chunk.
     *
     * @param x          The X coordinate.
     * @param z          The Z coordinate.
     * @param y          The Y coordinate.
     * @param blockLight The block light level.
     */
    public void setBlockLight(int x, int z, int y, int blockLight) {
        if (blockLight < 0 || blockLight >= 16)
            throw new IllegalArgumentException();

        this.blockLight[getIndex(x, z, y)] = (byte) blockLight;
    }

    /**
     * Serializes tile data into a byte array.
     *
     * @return The byte array populated with the tile data.
     */
    public byte[] serializeTileData() {
        byte[] dest = new byte[((WIDTH * HEIGHT * DEPTH * 5) / 2)];

        System.arraycopy(types, 0, dest, 0, types.length);

        int pos = types.length;

        pos = getData(dest, pos, metaData);
        pos = getData(dest, pos, skyLight);
        pos = getData(dest, pos, blockLight);
        return dest;
    }

    private int getData(byte[] dest, int pos, byte[] skyLight) {
        for (int i = 0; i < skyLight.length; i += 2) {
            byte light1 = skyLight[i];
            byte light2 = skyLight[i + 1];
            dest[pos++] = (byte) ((light2 << 4) | light1);
        }
        return pos;
    }
}
