package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BlockChangePacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UpdateSignPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.StringTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import org.pmw.tinylog.Logger;

import java.util.Map;

public class ServerUpdateTileEntityTranslator implements ModernToBetaHandler<ServerUpdateTileEntityPacket> {

    @Override
    public void translate(ServerUpdateTileEntityPacket packet, Session betaSession) {
        //TODO: More TileEntities
        if (packet.getType() != UpdatedTileType.SIGN)
            return;

        Position pos = packet.getPosition();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        CompoundTag tag = packet.getNBT();
        Map<String, Tag> strings = tag.getValue();

        String[] signLines = new String[4];

        for (int line = 0; line < 4; ++line) {
            signLines[line] = Utils.getSignText((StringTag) strings.get("Text" + (line + 1)));
        }

        Logger.info("------- SIGN TEXT -------");
        for (int i = 0, signLinesLength = signLines.length; i < signLinesLength; i++) {
            String signLine = signLines[i];
            Logger.info("Text{}: {}", i, signLine);
        }
        Logger.info("------- SIGN END -------");

        /*
         * https://wiki.vg/index.php?title=Protocol&oldid=14204#Open_Sign_Editor
         * There must already be a sign at the given location (which the client does not do automatically) - send a Block Change first.
         */
        betaSession.sendPacket(new BlockChangePacketData(x, y, z, 63 /* working somehow also on wall-sign */, 0));
        betaSession.sendPacket(new UpdateSignPacketData(x, y, z, new String[]{signLines[0], signLines[1], signLines[2], signLines[3]}));
    }
}