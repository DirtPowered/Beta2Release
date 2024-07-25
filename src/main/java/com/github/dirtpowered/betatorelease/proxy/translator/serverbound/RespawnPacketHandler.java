package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3RespawnPacketData;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.ClientRequest;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientRequestPacket;

public class RespawnPacketHandler implements BetaToModernHandler<V1_7_3RespawnPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3RespawnPacketData packetClass) {
        session.getModernClient().sendModernPacket(new ClientRequestPacket(ClientRequest.RESPAWN));
    }
}