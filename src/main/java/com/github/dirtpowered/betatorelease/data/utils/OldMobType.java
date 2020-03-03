package com.github.dirtpowered.betatorelease.data.utils;

import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;

public class OldMobType {

    public static int fromMobType(MobType value) {
        int id = -1;
        switch (value) {
            case CREEPER:
                id = 50;
                break;
            case PIG:
                id = 90;
                break;
            case COW:
                id = 92;
                break;
            case WOLF:
                id = 95;
                break;
            case GHAST:
                id = 56;
                break;
            case SHEEP:
                id = 91;
                break;
            case SLIME:
                id = 55;
                break;
            case SPIDER:
                id = 52;
                break;
            case ZOMBIE:
                id = 54;
                break;
            case CHICKEN:
                id = 93;
                break;
            case SQUID:
                id = 94;
                break;
            case SKELETON:
                id = 51;
                break;
            case ZOMBIE_PIGMAN:
                id = 57;
                break;
            default:
                break;
        }

        return id;
    }
}
