package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ServerChatTranslator implements ModernToBetaHandler<ServerChatPacket> {
    private static final int MAX_PACKET_LENGTH = 100;
    private static final int LINE_LENGTH = 64;

    @Override
    public void translate(ServerChatPacket packet, Session betaSession) {
        if (packet.getType() == MessageType.NOTIFICATION) // skip action bar messages
            return;

        String messageJson = packet.getMessage().toJsonString();
        String formattedMessage = TextComponent.toLegacyText(ComponentSerializer.parse(messageJson));

        if (formattedMessage.length() <= MAX_PACKET_LENGTH) {
            sendFormattedMessage(formattedMessage, betaSession);
            return;
        }

        int index = 0;
        String lastColorCode = "";

        while (index < formattedMessage.length()) {
            int endIndex = Math.min(index + MAX_PACKET_LENGTH, formattedMessage.length());
            String part = formattedMessage.substring(index, endIndex);
            StringBuilder parts = new StringBuilder();

            int i = 0;
            while (i < part.length()) {
                int lineEndIndex = Math.min(i + LINE_LENGTH, part.length());

                if (lineEndIndex < part.length() && part.charAt(lineEndIndex - 1) == ChatColor.COLOR_CHAR)
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

            if (endIndex < message.length() && message.charAt(endIndex - 1) == ChatColor.COLOR_CHAR)
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
            if (text.charAt(i) == ChatColor.COLOR_CHAR) {
                lastColorCode = text.substring(i, i + 2);
                i++;
            }
        }
        return lastColorCode;
    }
}