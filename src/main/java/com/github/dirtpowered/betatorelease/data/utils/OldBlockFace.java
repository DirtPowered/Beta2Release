package com.github.dirtpowered.betatorelease.data.utils;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;

public class OldBlockFace {

    /*
     * Value 	0 	1 	2 	3 	4 	5
     * Offset 	-Y 	+Y 	-Z 	+Z 	-X 	+X
     */
    public static BlockFace translateBlockFace(int face) {
        BlockFace blockFace;
        switch (face) {
            case 0:
                blockFace = BlockFace.DOWN;
                break;
            case 1:
                blockFace = BlockFace.UP;
                break;
            case 2:
                blockFace = BlockFace.NORTH;
                break;
            case 3:
                blockFace = BlockFace.SOUTH;
                break;
            case 4:
                blockFace = BlockFace.WEST;
                break;
            case 5:
                blockFace = BlockFace.EAST;
                break;
            default:
                blockFace = BlockFace.DOWN;
                break;
        }
        return blockFace;
    }
}
