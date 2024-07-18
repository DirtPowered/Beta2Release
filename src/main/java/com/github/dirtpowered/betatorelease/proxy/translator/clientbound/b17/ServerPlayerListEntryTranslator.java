package com.github.dirtpowered.betatorelease.proxy.translator.clientbound.b17;

import com.github.dirtpowered.betatorelease.data.entity.EntityPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntryAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;

public class ServerPlayerListEntryTranslator implements ModernToBetaHandler<ServerPlayerListEntryPacket> {

    @Override
    public void translate(ServerPlayerListEntryPacket packet, Session betaSession) {
        if (packet.getAction() == PlayerListEntryAction.ADD_PLAYER || packet.getAction() == PlayerListEntryAction.REMOVE_PLAYER) {
            refreshEntries(packet, betaSession);
        }
    }

    private void refreshEntries(ServerPlayerListEntryPacket packet, Session betaSession) {
        //TODO: remove when player leaves
        for (PlayerListEntry entry : packet.getEntries()) {
            GameProfile gameProfile = entry.getProfile();
            EntityPlayer player = new EntityPlayer(gameProfile.getId());
            player.setUsername(gameProfile.getName());

            betaSession.getServer().getPlayerCache().addPlayer(player);
        }
    }
}
