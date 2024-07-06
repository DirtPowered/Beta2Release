package com.github.dirtpowered.betatorelease.data.remap;

import java.util.Arrays;
import java.util.stream.IntStream;

public class BlockMappings {
    public static int[] blockMappings = new int[256];
    public static int[] itemMappings = new int[2048];

    static {
        Arrays.fill(blockMappings, 1); // default to stone
        Arrays.fill(itemMappings, 1); // default to stone

        // fill allowed items
        IntStream.range(0, 360).forEach(i -> itemMappings[i] = i);
        // fill allowed blocks
        IntStream.range(0, 97).forEach(i -> blockMappings[i] = i);

        // TODO: mappings loader
    }

    public static int getFixedBlockId(int blockId) {
        return blockMappings[blockId];
    }

    public static int getFixedBlockData(int blockId, int blockData) {
        return blockData; // TODO: implement block data remapping
    }

    public static int getFixedItemId(int itemId) {
        try {
            return itemMappings[itemId];
        } catch (ArrayIndexOutOfBoundsException e) {
            return 1;
        }
    }
}