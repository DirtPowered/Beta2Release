package com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UpdateSignPacketData;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.world.block.UpdatedTileType;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.opennbt.tag.builtin.CompoundTag;
import com.github.steveice10.opennbt.tag.builtin.Tag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

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
            try {
                signLines[line] = TextComponent.toPlainText(ComponentSerializer.parse(strings.get("Text" + (line + 1)).getValue() + ""));
            } catch (Exception e) {
                signLines[line] = "error";
            }
        }
        betaSession.sendPacket(new UpdateSignPacketData(x, y, z, new String[]{signLines[0], signLines[1], signLines[2], signLines[3]}));
    }
}