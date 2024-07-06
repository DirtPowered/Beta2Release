package com.github.dirtpowered.betatorelease.data.utils;

import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;

public class OldBlockFace {

    /*
     * Value 	0 	1 	2 	3 	4 	5
     * Offset 	-Y 	+Y 	-Z 	+Z 	-X 	+X
     */
    public static BlockFace translateBlockFace(int face) {
        return switch (face) {
            case 1 -> BlockFace.UP;
            case 2 -> BlockFace.NORTH;
            case 3 -> BlockFace.SOUTH;
            case 4 -> BlockFace.WEST;
            case 5 -> BlockFace.EAST;
            default -> BlockFace.DOWN;
        };
    }
}