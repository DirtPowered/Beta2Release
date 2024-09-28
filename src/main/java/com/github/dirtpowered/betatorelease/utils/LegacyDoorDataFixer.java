package com.github.dirtpowered.betatorelease.utils;

import com.github.dirtpowered.betatorelease.data.chunk.Block;
import com.github.dirtpowered.betatorelease.data.chunk.BlockStorage;
import com.github.dirtpowered.betatorelease.network.session.Session;

/**
 * Modern -> Legacy (pre 1.2) door data fixer
 * The code may look simple, but figuring out how it should work took ages.
 */
public class LegacyDoorDataFixer {

    public static int getLegacyDoorData(Session session, int x, int y, int z, int data) {
        BlockStorage storage = session.getBlockStorage();

        Block block = storage.getBlockAt(x, y, z);
        Block bottomBlock, topBlock;
        boolean topHalf = (data & 0x8) != 0;

        if (topHalf) {
            topBlock = block;
            bottomBlock = storage.getBlockAt(x, y - 1, z);
        } else {
            bottomBlock = block;
            topBlock = storage.getBlockAt(x, y + 1, z);
        }

        int lowerData = bottomBlock.getBlockData();
        return convertDataValues(topBlock, lowerData, topHalf);
    }

    private static int convertDataValues(Block topHalfBlock, int lowerData, boolean topHalf) {
        int upperData = topHalfBlock.getBlockData();

        int orientation = lowerData & 0x3;
        boolean open = (lowerData & 0x4) != 0;

        int legacyData = orientation;
        // only the top half-block has hinge information
        boolean hingeOnLeft = (upperData & 0x1) != 0;

        if (hingeOnLeft) {
            // invert orientation
            legacyData = switch (orientation) {
                case 0 -> 3;
                case 1 -> 0;
                case 2 -> 1;
                case 3 -> 2;
                default -> legacyData;
            };
            // if hinge is on the left, open-close state is inverted as well
            open = !open;
        }

        if (open) legacyData |= 0x4;
        if (topHalf) legacyData |= 0x8;
        return legacyData;
    }
}