package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3MapDataPacketData;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.world.map.MapData;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIcon;
import com.github.steveice10.mc.protocol.data.game.world.map.MapIconType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMapDataPacket;
import com.google.common.collect.Sets;

import java.util.Set;

public class ServerMapDataTranslator implements ModernToBetaHandler<ServerMapDataPacket> {

    @Override
    public void translate(ServerMapDataPacket packet, Session betaSession) {
        BetaPlayer player = betaSession.getBetaPlayer();
        MapIcon[] icons = packet.getIcons();

        if (icons != null && icons.length > 0) {
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
        }

        MapData data = packet.getData();
        if (data == null || data.getData() == null)
            return;

        int rows = data.getRows();
        int columns = data.getColumns();
        byte[] mapData = data.getData();

        int offsetX = data.getX();
        int offsetY = data.getY();

        Set<Integer> changedColumns = Sets.newHashSet();

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                int targetX = offsetX + x;
                int targetY = offsetY + y;

                if (targetX >= 128 || targetY >= 128)
                    continue;

                int idx = targetY * 128 + targetX;
                byte newColor = mapData[y * columns + x];

                player.getCurrentMapTable()[idx] = newColor;
                changedColumns.add(targetX);
            }
        }

        for (int col : changedColumns) {
            byte[] columnData = new byte[131]; // 128 map pixels + 3 header bytes
            columnData[0] = 0;
            columnData[1] = (byte) col;
            columnData[2] = 0;

            for (int row = 0; row < 128; row++) {
                byte color = player.getCurrentMapTable()[row * 128 + col];
                columnData[row + 3] = fixColor(color);
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