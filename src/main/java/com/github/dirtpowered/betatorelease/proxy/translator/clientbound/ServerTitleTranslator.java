package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3ChatPacketData;
import com.github.dirtpowered.betatorelease.data.lang.LangStorage;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.dirtpowered.betatorelease.utils.LegacyTextWrapper;
import com.github.steveice10.mc.protocol.data.game.TitleAction;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerTitlePacket;

public class ServerTitleTranslator implements ModernToBetaHandler<ServerTitlePacket> {

    @Override
    public void translate(ServerTitlePacket packet, Session betaSession) {
        if (betaSession.getServer().getConfiguration().isSkipTitleMessages())
            return;

        TitleAction action = packet.getAction();
        String text;

        switch (action) {
            case ACTION_BAR -> text = packet.getActionBar().toJsonString();
            case TITLE -> text = packet.getTitle().toJsonString();
            case SUBTITLE -> text = packet.getSubtitle().toJsonString();
            default -> {
                return;
            }
        }
        text = LangStorage.translate(text, true); // apply args
        // send messages in parts, so the client can display them correctly
        for (String s : LegacyTextWrapper.wrapText(text)) {
            betaSession.sendPacket(new V1_7_3ChatPacketData(s));
        }
    }
}