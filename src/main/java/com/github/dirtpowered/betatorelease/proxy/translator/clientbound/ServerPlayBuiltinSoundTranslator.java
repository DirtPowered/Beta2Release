package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3SoundEffectPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.world.sound.BuiltinSound;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;

public class ServerPlayBuiltinSoundTranslator implements ModernToBetaHandler<ServerPlayBuiltinSoundPacket> {

    @Override
    public void translate(ServerPlayBuiltinSoundPacket packet, Session betaSession) {
        BuiltinSound sound = packet.getSound();

        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();

        if (sound == BuiltinSound.BLOCK_WOODEN_DOOR_OPEN
                || sound == BuiltinSound.BLOCK_WOODEN_DOOR_CLOSE
                || sound == BuiltinSound.BLOCK_IRON_DOOR_OPEN
                || sound == BuiltinSound.BLOCK_IRON_DOOR_CLOSE
                || sound == BuiltinSound.BLOCK_IRON_TRAPDOOR_OPEN
                || sound == BuiltinSound.BLOCK_IRON_TRAPDOOR_CLOSE
                || sound == BuiltinSound.BLOCK_WOODEN_TRAPDOOR_OPEN
                || sound == BuiltinSound.BLOCK_WOODEN_TRAPDOOR_CLOSE) {

            // fix door, trapdoor sounds
            betaSession.sendPacket(new V1_7_3SoundEffectPacketData(1003, (int) x, (int) y, (int) z, 0));
        } else if (sound == BuiltinSound.ENTITY_ARROW_SHOOT
                || sound == BuiltinSound.ENTITY_SKELETON_SHOOT) {
            betaSession.sendPacket(new V1_7_3SoundEffectPacketData(1002, (int) x, (int) y, (int) z, 0));
        }
    }
}