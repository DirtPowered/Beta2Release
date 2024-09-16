package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;

public class ServerChatTranslator implements ModernToBetaHandler<ServerChatPacket> {
    private static final int MAX_PACKET_LENGTH = 100;
    private static final int LINE_LENGTH = 64;
    private static final char COLOR_CHAR = '§';

    @Override
    public void translate(ServerChatPacket packet, Session betaSession) {
        if (packet.getType() == MessageType.NOTIFICATION) // skip action bar messages
            return;

        String legacy = LangStorage.translate(packet.getMessage().toJsonString(), false);

        if (legacy.length() <= MAX_PACKET_LENGTH) {
            sendFormattedMessage(legacy, betaSession);
            return;
        }

        int index = 0;
        String lastColorCode = "";

        while (index < legacy.length()) {
            int endIndex = Math.min(index + MAX_PACKET_LENGTH, legacy.length());
            String part = legacy.substring(index, endIndex);
            StringBuilder parts = new StringBuilder();

            int i = 0;
            while (i < part.length()) {
                int lineEndIndex = Math.min(i + LINE_LENGTH, part.length());

                if (lineEndIndex < part.length() && part.charAt(lineEndIndex - 1) == COLOR_CHAR)
                    lineEndIndex--;

                String linePart = part.substring(i, lineEndIndex);

                if (!lastColorCode.isEmpty())
                    linePart = lastColorCode + linePart;

                parts.append(linePart);
                betaSession.sendPacket(new V1_7_3ChatPacketData(parts.toString()));
                parts.setLength(0); // reset

                lastColorCode = getLastColorCode(linePart);
                i = lineEndIndex;
            }
            index = endIndex;
        }
    }

    private void sendFormattedMessage(String message, Session betaSession) {
        String lastColorCode = "";
        int index = 0;

        while (index < message.length()) {
            int endIndex = Math.min(index + ServerChatTranslator.LINE_LENGTH, message.length());

            if (endIndex < message.length() && message.charAt(endIndex - 1) == COLOR_CHAR)
                endIndex--;

            String part = message.substring(index, endIndex);
            if (!lastColorCode.isEmpty())
                part = lastColorCode + part;

            betaSession.sendPacket(new V1_7_3ChatPacketData(part));

            lastColorCode = getLastColorCode(part);
            index = endIndex;
        }
    }

    private String getLastColorCode(String text) {
        String lastColorCode = "";
        int length = text.length();

        for (int i = 0; i < length - 1; i++) {
            if (text.charAt(i) == COLOR_CHAR) {
                lastColorCode = text.substring(i, i + 2);
                i++;
            }
        }
        return lastColorCode;
    }
}