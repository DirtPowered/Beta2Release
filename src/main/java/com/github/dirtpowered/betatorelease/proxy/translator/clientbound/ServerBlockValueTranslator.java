package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PlayNoteblockPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.world.block.value.NoteBlockValue;
import com.github.steveice10.mc.protocol.data.game.world.block.value.NoteBlockValueType;
import com.github.steveice10.mc.protocol.data.game.world.block.value.PistonValue;
import com.github.steveice10.mc.protocol.data.game.world.block.value.PistonValueType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;

public class ServerBlockValueTranslator implements ModernToBetaHandler<ServerBlockValuePacket> {

    @Override
    public void translate(ServerBlockValuePacket packet, Session betaSession) {
        int x = packet.getPosition().getX();
        int y = packet.getPosition().getY();
        int z = packet.getPosition().getZ();

        if (packet.getValue() instanceof NoteBlockValue noteBlockValue) {
            int instrument = (packet.getType() instanceof NoteBlockValueType noteBlockType) ?
                    getInstrumentType(noteBlockType) : 0;

            betaSession.sendPacket(new V1_7_3PlayNoteblockPacketData(x, y, z, instrument, noteBlockValue.getPitch()));
            return;
        }

        if (packet.getValue() instanceof PistonValue pistonValue) {
            int instrument = (packet.getType() == PistonValueType.PUSHING) ? 0 : 1;

            betaSession.sendPacket(new V1_7_3PlayNoteblockPacketData(x, y, z, instrument, pistonValue.ordinal()));
        }
    }

    private int getInstrumentType(NoteBlockValueType sound) {
        return switch (sound) {
            case HARP, BELL, GUITAR, CHIME, XYLOPHONE, FLUTE -> 0; // remap unsupported instruments to harp
            case DOUBLE_BASS -> 1;
            case SNARE_DRUM -> 2;
            case HI_HAT -> 3;
            case BASS_DRUM -> 4;
        };
    }
}