package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;

import java.util.ArrayList;
import java.util.List;

public class ServerChatTranslator implements ModernToBetaHandler<ServerChatPacket> {
    private static final int MAX_PACKET_LENGTH = 100;
    private static final int LINE_LENGTH = 64;
    private static final char COLOR_CHAR = '§';

    @Override
    public void translate(ServerChatPacket packet, Session betaSession) {
        if (packet.getType() == MessageType.NOTIFICATION) // skip action bar messages
            return;

        String legacy = LangStorage.translate(packet.getMessage().toJsonString(), false);
        sendFormattedMessage(legacy, betaSession);
    }

    public static List<String> formatMessage(String message) {
        List<String> formattedParts = new ArrayList<>();
        int index = 0;
        String lastColorCode = "";

        while (index < message.length()) {
            int endIndex = Math.min(index + MAX_PACKET_LENGTH, message.length());
            String part = message.substring(index, endIndex);
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
                formattedParts.add(parts.toString());
                parts.setLength(0);

                lastColorCode = getLastColorCode(linePart);
                i = lineEndIndex;
            }
            index = endIndex;
        }
        return formattedParts;
    }

    public void sendFormattedMessage(String message, Session betaSession) {
        List<String> messageParts = formatMessage(message);

        for (String part : messageParts) {
            betaSession.sendPacket(new V1_7_3ChatPacketData(part));
        }
    }

    private static String getLastColorCode(String text) {
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