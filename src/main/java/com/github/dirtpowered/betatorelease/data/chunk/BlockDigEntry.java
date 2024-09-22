package com.github.dirtpowered.betatorelease.data.chunk;

import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import lombok.Data;

@Data
public class BlockDigEntry {
    private PlayerAction playerAction;
    private Position position;
    private BlockFace blockFace;
}