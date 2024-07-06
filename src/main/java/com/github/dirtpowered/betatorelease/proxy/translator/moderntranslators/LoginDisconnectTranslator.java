package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

public class LoginDisconnectTranslator implements ModernToBetaHandler<LoginDisconnectPacket> {

    @Override
    public void translate(LoginDisconnectPacket packet, Session betaSession) {
        String reason = packet.getReason().getFullText();
        String formattedMessage = TextComponent.toLegacyText(ComponentSerializer.parse(reason));

        betaSession.disconnect(formattedMessage);
    }
}