package com.github.dirtpowered.betatorelease.data.chunk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Block {
    private final int x;
    private final int y;
    private final int z;

    @Setter
    private int blockId;

    private final int blockData;
}