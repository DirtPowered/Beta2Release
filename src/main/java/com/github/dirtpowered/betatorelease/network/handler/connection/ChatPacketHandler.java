package com.github.dirtpowered.betatorelease.network.handler.connection;

import com.github.dirtpowered.betaprotocollib.packet.data.ChatPacketData;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.pmw.tinylog.Logger;

public class ChatPacketHandler implements BetaToModernHandler<ChatPacketData> {

    @Override
    public void handlePacket(Session session, ChatPacketData packetClass) {
        String message = packetClass.getMessage();
        Logger.info("received chat message: {}", message);

        session.getModernClient().sendModernPacket(new ClientChatPacket(message));
    }
}
