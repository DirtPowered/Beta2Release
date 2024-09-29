package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3BlockChangePacketData;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.data.chunk.BlockStorage;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.LegacyDoorDataFixer;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockChangeRecord;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;

public class ServerBlockChangeTranslator implements ModernToBetaHandler<ServerBlockChangePacket> {

    @Override
    public void translate(ServerBlockChangePacket packet, Session betaSession) {
        BlockChangeRecord record = packet.getRecord();
        Position position = record.getPosition();
        BlockState block = record.getBlock();

        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();

        int blockId = block.getId();
        int blockData = block.getData();

        // Check if chunk is loaded. Beta 1.7.3 client will crash if block is changed in unloaded chunk
        BlockStorage storage = betaSession.getBlockStorage();
        if (!storage.isChunkLoaded(x >> 4, z >> 4)) {
            Main.LOGGER.warn("Attempted to change block at unloaded chunk: {} {} {}", x, y, z);
            return;
        }

        BlockMappings.RemappedBlock remap = BlockMappings.getRemappedBlock(blockId, blockData);
        storage.setBlockAt(x, y, z, remap.blockId(), remap.blockData());

        int remapped = remap.blockData();
        if (Utils.isDoor(remap.blockId()))
            remapped = LegacyDoorDataFixer.getLegacyDoorData(betaSession, x, y, z, blockData);

        betaSession.sendPacket(new V1_7_3BlockChangePacketData(x, y, z, remap.blockId(), remapped));
    }
}