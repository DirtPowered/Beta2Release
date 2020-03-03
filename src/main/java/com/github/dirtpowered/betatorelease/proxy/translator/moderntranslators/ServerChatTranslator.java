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
        if (packet.getType() != MessageType.SYSTEM)
            return;

        String messageJson = packet.getMessage().toJsonString();
        String formattedMessage = TextComponent.toLegacyText(ComponentSerializer.parse(messageJson));

        String old = null;
        String additionalStr = null;

        if (formattedMessage.length() > 119) {
            old = formattedMessage.substring(0, 119);
            additionalStr = formattedMessage.substring(119);
        }

        betaSession.sendMessage(old != null ? old : formattedMessage);
        if (additionalStr != null) {
            betaSession.sendMessage(additionalStr);
        }
    }
}
