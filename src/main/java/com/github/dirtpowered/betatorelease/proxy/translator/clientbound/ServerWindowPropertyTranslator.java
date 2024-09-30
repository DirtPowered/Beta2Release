package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3UpdateProgressPacketData;
import com.github.dirtpowered.betatorelease.data.tile.FurnaceFuelCache;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.window.WindowType;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;

public class ServerWindowPropertyTranslator implements ModernToBetaHandler<ServerWindowPropertyPacket> {

    @Override
    public void translate(ServerWindowPropertyPacket packet, Session betaSession) {
        BetaPlayer player = betaSession.getBetaPlayer();
        // skip if current opened inventory is not a furnace
        if (player.getWindowTypeMap().get(packet.getWindowId()) == null
                || !player.getWindowTypeMap().get(packet.getWindowId()).equals(WindowType.FURNACE))
            return;

        int updateType = packet.getRawProperty();
        int value = packet.getValue();

        boolean updateFuel = updateType == 0;
        FurnaceFuelCache cache = player.getFurnaceFuelCache();

        int progressValue = -1;

        switch (updateType) {
            case 0 -> progressValue = (value * cache.getMaxProgress()) / cache.getFuel(); // fuel
            case 1 -> cache.setFuel(value); // max fuel (1600)
            case 2 -> progressValue = value; // progress
            case 3 -> cache.setMaxProgress(value); // max progress (200)
        }

        if (progressValue == -1)
            return;

        betaSession.sendPacket(new V1_7_3UpdateProgressPacketData(packet.getWindowId(), updateFuel ? 1 : 0, progressValue));
    }
}