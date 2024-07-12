package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ServerChatTranslator implements ModernToBetaHandler<ServerChatPacket> {

    @Override
    public void translate(ServerChatPacket packet, Session betaSession) {
        if (packet.getType() == MessageType.NOTIFICATION) // skip action bar messages
            return;

        String messageJson = packet.getMessage().toJsonString();
        String formattedMessage = TextComponent.toLegacyText(ComponentSerializer.parse(messageJson));

        // we need to split the message if it's too long
        if (formattedMessage.length() > 100) {
            int index = 0;
            while (index < formattedMessage.length()) {
                int endIndex = Math.min(index + 100, formattedMessage.length());
                String part = formattedMessage.substring(index, endIndex);
                betaSession.sendMessage(part);
                index = endIndex;
            }
            return;
        }

        betaSession.sendMessage(formattedMessage);
    }
}