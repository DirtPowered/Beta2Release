package com.github.dirtpowered.betatorelease.data.chunk;

public class Block {

    private int x;
    private int y;
    private int z;
    private int blockId;
    private int blockData;

    public Block(int x, int y, int z, int blockId, int blockData) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockId = blockId;
        this.blockData = blockData;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public int getBlockData() {
        return blockData;
    }
}
