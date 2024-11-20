package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3SoundEffectPacketData;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.data.remap.BlockMappings;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.effect.*;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;

public class ServerPlayEffectTranslator implements ModernToBetaHandler<ServerPlayEffectPacket> {

    @Override
    public void translate(ServerPlayEffectPacket packet, Session betaSession) {
        Position position = packet.getPosition();
        int effectId = -1;
        int packedData = 0;

        WorldEffect effect = packet.getEffect();

        if (effect.equals(ParticleEffect.BREAK_BLOCK)) {
            if (packet.getData() instanceof BreakBlockEffectData data) {
                int remappedBlockId = BlockMappings.getRemappedBlock(data.getBlockState().getId()).blockId();
                packedData = (byte) (remappedBlockId + (data.getBlockState().getData() << 12));
                effectId = 2001;
            }
        } else if (effect.equals(ParticleEffect.SMOKE)) {
            effectId = 2000;
        } else if (effect.equals(SoundEffect.BLOCK_DISPENSER_LAUNCH)) {
            effectId = 1002;
        } else if (effect.equals(SoundEffect.BLOCK_DISPENSER_DISPENSE)) {
            effectId = 1000;
        } else if (effect.equals(SoundEffect.BLOCK_DISPENSER_FAIL)) {
            effectId = 1001;
        } else if (effect.equals(SoundEffect.BLOCK_FIRE_EXTINGUISH)) {
            effectId = 1004;
        } else if (effect.equals(SoundEffect.RECORD) && packet.getData() instanceof RecordEffectData data) {
            int recordId = data.getRecordId();
            if (recordId != 2256 && recordId != 2257) {
                Main.LOGGER.warn("Unknown record id: {}", recordId);
                return;
            }
            effectId = 1005;
            packedData = recordId;
        } else if (effect.equals(SoundEffect.BLOCK_IRON_DOOR_CLOSE)) {
            // mcprotocollib has wrong mapping for this sound
            effectId = 1005;
        }

        if (effectId != -1) {
            betaSession.sendPacket(new V1_7_3SoundEffectPacketData(effectId, position.getX(), position.getY(), position.getZ(), packedData));
        }
    }
}