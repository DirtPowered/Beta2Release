package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.SoundEffectPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.effect.BreakBlockEffectData;
import com.github.steveice10.mc.protocol.data.game.world.effect.ParticleEffect;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayEffectPacket;

public class ServerPlayEffectTranslator implements ModernToBetaHandler<ServerPlayEffectPacket> {

    @Override
    public void translate(ServerPlayEffectPacket packet, Session betaSession) {
        Utils.debug(packet);
        Position position = packet.getPosition();

        if (packet.getEffect() == ParticleEffect.BREAK_BLOCK && packet.getData() instanceof BreakBlockEffectData data) {
            byte packedData = (byte) (data.getBlockState().getId() + (data.getBlockState().getData() << 12));

            betaSession.sendPacket(new SoundEffectPacketData(2001, position.getX(), position.getY(), position.getZ(), packedData));
        } else if (packet.getEffect() == ParticleEffect.SMOKE) {
            betaSession.sendPacket(new SoundEffectPacketData(2000, position.getX(), position.getY(), position.getZ(), 0));
        }
    }
}