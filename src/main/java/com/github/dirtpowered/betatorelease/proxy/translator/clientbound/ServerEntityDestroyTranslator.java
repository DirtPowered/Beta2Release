package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3EntityDestroyPacketData;
import com.github.dirtpowered.betatorelease.data.entity.EntityVehicle;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;

public class ServerEntityDestroyTranslator implements ModernToBetaHandler<ServerEntityDestroyPacket> {

    @Override
    public void translate(ServerEntityDestroyPacket packet, Session betaSession) {
        for (int entityId : packet.getEntityIds()) {
            betaSession.sendPacket(new V1_7_3EntityDestroyPacketData(entityId));
            ejectVehicle(betaSession, betaSession.getEntityCache().removeEntity(entityId));
        }
    }

    private static void ejectVehicle(Session betaSession, Entity entity) {
        if (!(entity instanceof EntityVehicle vehicle))
            return;

        BetaPlayer player = betaSession.getBetaPlayer();

        if (!player.isInVehicle() || player.getVehicleEntityId() != vehicle.getEntityId())
            return;

        betaSession.getModernClient().sendModernPacket(new ClientPlayerStatePacket(player.getEntityId(), PlayerState.STOP_SNEAKING));
        player.setInVehicle(false, -1);
        player.setSneaking(false);
    }
}