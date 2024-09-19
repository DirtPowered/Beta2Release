package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3SoundEffectPacketData;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.effect.BreakBlockEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.ParticleEffect;
import com.github.steveice10.mc.protocol.data.game.world.effect.RecordEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.SoundEffect;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;

public class ServerPlayEffectTranslator implements ModernToBetaHandler<ServerPlayEffectPacket> {

    @Override
    public void translate(ServerPlayEffectPacket packet, Session betaSession) {
        Position position = packet.getPosition();

        if (packet.getEffect() == ParticleEffect.BREAK_BLOCK && packet.getData() instanceof BreakBlockEffectData data) {
            byte packedData = (byte) (BlockMappings.getRemappedBlock(data.getBlockState().getId()).blockId() + (data.getBlockState().getData() << 12));

            betaSession.sendPacket(new V1_7_3SoundEffectPacketData(2001, position.getX(), position.getY(), position.getZ(), packedData));
        } else if (packet.getEffect() == ParticleEffect.SMOKE) {
            betaSession.sendPacket(new V1_7_3SoundEffectPacketData(2000, position.getX(), position.getY(), position.getZ(), 0));
        }

        // handle jukebox discs
        if (packet.getEffect() == SoundEffect.RECORD && packet.getData() instanceof RecordEffectData data) {
            int recordId = data.getRecordId();

            if (recordId != 2256 && recordId != 2257) {
                Main.LOGGER.warn("Unknown record id: " + recordId);
                return;
            }
            betaSession.sendPacket(new V1_7_3SoundEffectPacketData(1005, position.getX(), position.getY(), position.getZ(), recordId));
        } else if (packet.getEffect() == SoundEffect.BLOCK_IRON_DOOR_CLOSE) {
            // mcprotocollib has wrong mapping for this sound
            betaSession.sendPacket(new V1_7_3SoundEffectPacketData(1005, position.getX(), position.getY(), position.getZ(), 0));
        }
    }
}