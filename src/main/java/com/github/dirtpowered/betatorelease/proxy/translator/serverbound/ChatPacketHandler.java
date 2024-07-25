package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;

public class ChatPacketHandler implements BetaToModernHandler<V1_7_3ChatPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3ChatPacketData packetClass) {
        session.getModernClient().sendModernPacket(new ClientChatPacket(packetClass.getMessage()));
    }
}