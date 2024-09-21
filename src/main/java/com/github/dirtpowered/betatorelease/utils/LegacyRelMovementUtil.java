package com.github.dirtpowered.betatorelease.utils;

import java.util.ArrayList;
import java.util.List;

public class LegacyRelMovementUtil {
    private static final int MAX_RELATIVE_SIZE = 127;

    public static List<RelPos> toLegacyRelMove(int relX, int relY, int relZ) {
        List<RelPos> movements = new ArrayList<>();

        while (Math.abs(relX) > MAX_RELATIVE_SIZE || Math.abs(relY) > MAX_RELATIVE_SIZE || Math.abs(relZ) > MAX_RELATIVE_SIZE) {
            int moveX = clamp(relX);
            int moveY = clamp(relY);
            int moveZ = clamp(relZ);

            movements.add(new RelPos(moveX, moveY, moveZ));

            relX -= moveX;
            relY -= moveY;
            relZ -= moveZ;
        }

        if (relX != 0 || relY != 0 || relZ != 0)
            movements.add(new RelPos(relX, relY, relZ));

        return movements;
    }

    private static int clamp(int value) {
        return Math.max(-127, Math.min(value, LegacyRelMovementUtil.MAX_RELATIVE_SIZE));
    }

    public record RelPos(int x, int y, int z) {
        // empty
    }
}