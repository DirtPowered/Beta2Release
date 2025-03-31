package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3LoginPacketData;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.utils.MojangAuthUtil;
import com.github.steveice10.mc.auth.util.UUIDSerializer;

public class LoginPacketHandler implements BetaToModernHandler<V1_7_3LoginPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3LoginPacketData packetClass) {
        ProtocolState protocolState = session.getProtocolState();

        if (protocolState != ProtocolState.LOGIN)
            return;

        // limit client version to 1.7.3, if enabled
        if (session.getServer().getConfiguration().isStrictVersionCheck()) {
            if (packetClass.getEntityId() != MinecraftVersion.B_1_7_3.getProtocolVersion()) {
                session.disconnect("Invalid client version, please use 1.7.3");
                return;
            }
        }
        session.setProtocolState(ProtocolState.PLAY);
        session.setPlayerName(packetClass.getPlayerName());

        // handle authentication
        if (session.getServer().getConfiguration().isOnlineMode()) {
            handleAuthAsync(session, packetClass.getPlayerName(), session.getBetaPlayer().getServerId());
        } else {
            session.getModernClient().connect(session); // connect to remote modern server
        }
    }

    private void handleAuthAsync(Session session, String playername, String serverId) {
        String translated = LangStorage.translateKeyWithParams("multiplayer.disconnect.unverified_username", playername);

        MojangAuthUtil.hasJoined(playername, serverId).thenAccept(pair -> {
            int code = pair.getLeft();
            String response = pair.getRight();

            if (code != -1 && code != 204 && response.contains("\"id\"") && response.contains("\"name\"")) {
                String uuid = response.split("\"id\" : \"")[1].split("\",")[0];
                session.getBetaPlayer().setUuid(UUIDSerializer.fromString(uuid)); // for future use

                // connect immediately after authentication
                session.getModernClient().connect(session);
            } else {
                Main.LOGGER.warn("Failed to authenticate player '{}', code: {}", playername, code);
                session.disconnect(translated);
                session.getChannel().close();
            }
        }).exceptionally(ex -> {
            Main.LOGGER.error("Exception during authentication for '{}': {}", playername, ex.getMessage());
            session.disconnect(translated);
            session.getChannel().close();
            return null;
        });
    }
}