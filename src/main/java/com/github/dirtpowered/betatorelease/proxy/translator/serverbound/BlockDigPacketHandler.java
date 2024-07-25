package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3BlockDigPacketData;
import com.github.dirtpowered.betatorelease.data.utils.OldBlockFace;
import com.github.dirtpowered.betatorelease.data.utils.OldPlayerAction;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.mc.protocol.data.game.entity.metadata.Position;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerAction;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockFace;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerActionPacket;

public class BlockDigPacketHandler implements BetaToModernHandler<V1_7_3BlockDigPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3BlockDigPacketData packetClass) {
        int status = packetClass.getStatus();
        int face = packetClass.getFace();

        BlockFace blockFace = OldBlockFace.translateBlockFace(face);
        PlayerAction playerAction = OldPlayerAction.getPlayerAction(status);

        if (playerAction == null || blockFace == null)
            return;

        int x = packetClass.getX();
        int y = packetClass.getY();
        int z = packetClass.getZ();

        session.getModernClient().sendModernPacket(new ClientPlayerActionPacket(playerAction, new Position(x, y, z), blockFace));
    }
}