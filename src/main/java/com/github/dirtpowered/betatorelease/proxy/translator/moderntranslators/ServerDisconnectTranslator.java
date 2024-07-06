package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class ServerDisconnectTranslator implements ModernToBetaHandler<ServerDisconnectPacket> {

    @Override
    public void translate(ServerDisconnectPacket packet, Session betaSession) {
        String reason = packet.getReason().getFullText();
        String formattedMessage = TextComponent.toLegacyText(ComponentSerializer.parse(reason));

        betaSession.disconnect(formattedMessage);
    }
}