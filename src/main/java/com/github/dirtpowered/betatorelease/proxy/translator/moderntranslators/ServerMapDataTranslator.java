package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.MapDataPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.world.map.MapData;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;

public class ServerMapDataTranslator implements ModernToBetaHandler<ServerMapDataPacket> {

    @Override
    public void translate(ServerMapDataPacket packet, Session betaSession) {
        MapData data = packet.getData();

        if (data == null || data.getRows() != 128 || data.getColumns() != 128)
            return; // skip invalid data which can't be translated 1:1

        // TODO: cursors, icons, etc
        byte[] a = data.getData();

        for (int x = 0; x < 128; ++x) {
            byte[] bytes = new byte[131];
            bytes[1] = (byte) x;

            for (int y = 0; y < 128; ++y) {
                byte color = a[y * 128 + x];
                bytes[y + 3] = fixColor(color);
            }
            betaSession.sendPacket(new MapDataPacketData(packet.getMapId(), bytes));
        }
    }

    private byte fixColor(byte color) {
        return color >= 4 && color <= 52 ? color : 0; // TODO: remap modern colors to old equivalent
    }
}