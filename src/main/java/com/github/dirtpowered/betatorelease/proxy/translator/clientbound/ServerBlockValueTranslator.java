package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3PlayNoteblockPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.world.block.value.NoteBlockValue;
import com.github.steveice10.mc.protocol.data.game.world.block.value.NoteBlockValueType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockValuePacket;

public class ServerBlockValueTranslator implements ModernToBetaHandler<ServerBlockValuePacket> {

    @Override
    public void translate(ServerBlockValuePacket packet, Session betaSession) {
        if (!(packet.getValue() instanceof NoteBlockValue value))
            return;

        int instrument = 0;
        int pitch = value.getPitch();

        if (packet.getType() instanceof NoteBlockValueType noteBlockValueType)
            instrument = getInstrumentType(noteBlockValueType);

        betaSession.sendPacket(new V1_7_3PlayNoteblockPacketData(packet.getPosition().getX(), packet.getPosition().getY(), packet.getPosition().getZ(), instrument, pitch));
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