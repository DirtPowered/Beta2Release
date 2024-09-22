package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.LegacyTextWrapper;
import com.github.steveice10.mc.protocol.data.game.MessageType;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;

public class ServerChatTranslator implements ModernToBetaHandler<ServerChatPacket> {

    @Override
    public void translate(ServerChatPacket packet, Session betaSession) {
        if (packet.getType() == MessageType.NOTIFICATION) // skip action bar messages
            return;

        String legacy = LangStorage.translate(packet.getRawMessage(), false);
        sendFormattedMessage(legacy, betaSession);
    }

    private void sendFormattedMessage(String message, Session betaSession) {
        for (String part : LegacyTextWrapper.wrapText(message)) {
            betaSession.sendPacket(new V1_7_3ChatPacketData(part));
        }
    }
}