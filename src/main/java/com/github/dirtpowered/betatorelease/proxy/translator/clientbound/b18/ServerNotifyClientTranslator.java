package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b18;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.BedAndWeatherPacketData;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;

public class ServerNotifyClientTranslator implements ModernToBetaHandler<ServerNotifyClientPacket> {

    @Override
    public void translate(ServerNotifyClientPacket packet, Session betaSession) {
        int state = switch (packet.getNotification()) {
            case START_RAIN -> 2;
            case STOP_RAIN -> 1;
            case INVALID_BED -> 0;
            case CHANGE_GAMEMODE -> 3;
            default -> -1;
        };

        if (state == -1)
            return;

        BetaPlayer player = betaSession.getBetaPlayer();
        int gameMode = player.getGameMode();

        if (packet.getValue() instanceof GameMode mode) {
            gameMode = switch (mode) {
                case SURVIVAL, ADVENTURE -> 0;
                case CREATIVE, SPECTATOR -> 1;
            };
            player.setGameMode(gameMode);
        }
        betaSession.sendPacket(new BedAndWeatherPacketData(state, gameMode));
    }
}