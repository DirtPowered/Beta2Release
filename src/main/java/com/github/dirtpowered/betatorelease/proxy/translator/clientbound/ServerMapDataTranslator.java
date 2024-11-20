package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3MapDataPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.world.map.MapData;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIcon;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIconType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;

public class ServerMapDataTranslator implements ModernToBetaHandler<ServerMapDataPacket> {

    @Override
    public void translate(ServerMapDataPacket packet, Session betaSession) {
        MapData data = packet.getData();

        if (data == null || data.getRows() != 128 || data.getColumns() != 128) {
            MapIcon[] icons = packet.getIcons();
            byte[] iconData = new byte[icons.length * 3 + 1]; // 3 bytes per icon + 1 byte for header
            iconData[0] = 1; // show icons

            for (int i = 0; i < icons.length; i++) {
                MapIcon icon = icons[i];
                byte direction = (byte) icon.getIconRotation();

                iconData[i * 3 + 1] = (byte) ((direction << 4) | toLegacyIconType(icon.getIconType()));
                iconData[i * 3 + 2] = (byte) icon.getCenterX();
                iconData[i * 3 + 3] = (byte) icon.getCenterZ();
            }
            betaSession.sendPacket(new V1_7_3MapDataPacketData(packet.getMapId(), iconData));
            return;
        }

        byte[] mapData = data.getData();

        for (int x = 0; x < 128; ++x) {
            byte[] columnData = new byte[131]; // 128 map pixels + 3 header bytes
            columnData[1] = (byte) x;

            for (int y = 0; y < 128; ++y) {
                byte color = mapData[y * 128 + x];
                columnData[y + 3] = fixColor(color);
            }
            betaSession.sendPacket(new V1_7_3MapDataPacketData(packet.getMapId(), columnData));
        }
    }

    private byte toLegacyIconType(MapIconType iconType) {
        return switch (iconType) {
            case GREEN_ARROW -> 1;
            case RED_ARROW, TEMPLE, MANSION, RED_POINTER -> 2;
            case BLUE_ARROW -> 3;
            case WHITE_CROSS -> 4;
            default -> 0;
        };
    }

    private byte fixColor(byte color) {
        return color >= 4 && color <= 52 ? color : 0; // TODO: remap modern colors to old equivalent
    }
}