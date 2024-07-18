package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b18;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.KickDisconnectPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.ServerListPingPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import net.md_5.bungee.api.ChatColor;

public class ServerListPingPacketHandler implements BetaToModernHandler<ServerListPingPacketData> {

    @Override
    public void handlePacket(Session session, ServerListPingPacketData packetClass) {
        String legacy = "A B2R Proxy Server" + ChatColor.COLOR_CHAR + 0 + ChatColor.COLOR_CHAR + 20;

        session.sendPacket(new KickDisconnectPacketData(legacy));
    }
}