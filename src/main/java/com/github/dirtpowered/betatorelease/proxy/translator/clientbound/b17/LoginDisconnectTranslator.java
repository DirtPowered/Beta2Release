package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;

public class LoginDisconnectTranslator implements ModernToBetaHandler<LoginDisconnectPacket> {

    @Override
    public void translate(LoginDisconnectPacket packet, Session betaSession) {
        String reason = packet.getReason().getFullText();

        // the beta client is capable of displaying only up to 100 characters
        if (reason.length() > 100)
            reason = reason.substring(0, 100);

        betaSession.disconnect(reason);
    }
}