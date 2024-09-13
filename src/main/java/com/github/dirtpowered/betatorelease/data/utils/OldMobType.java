package com.github.dirtpowered.betatorelease.data.utils;

import com.github.steveice10.mc.protocol.data.game.entity.type.MobType;

import java.util.Objects;

public class OldMobType {

    public static byte fromMobType(MobType value) {
        byte id;
        if (Objects.requireNonNull(value) == MobType.CREEPER) {
            id = 50;
        } else if (value == MobType.PIG) {
            id = 90;
        } else if (value == MobType.COW) {
            id = 92;
        } else if (value == MobType.WOLF) {
            id = 95;
        } else if (value == MobType.GHAST) {
            id = 56;
        } else if (value == MobType.SHEEP) {
            id = 91;
        } else if (value == MobType.SLIME || value == MobType.MAGMA_CUBE) {
            id = 55;
        } else if (value == MobType.SPIDER) {
            id = 52;
        } else if (value == MobType.ZOMBIE) {
            id = 54;
        } else if (value == MobType.CHICKEN) {
            id = 93;
        } else if (value == MobType.SQUID) {
            id = 94;
        } else if (value == MobType.SKELETON || value == MobType.WITHER_SKELETON) {
            id = 51;
        } else if (value == MobType.ZOMBIE_PIGMAN) {
            id = 57;
        } else if (value == MobType.ENDERMAN) {
            id = 54;
        } else {
            id = 90; // All unknown entities should be visible anyway. Enderdragon too. Flying pig *_*
        }
        return id;
    }
}