package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3AttachEntityPacketData;
import com.github.dirtpowered.betatorelease.data.entity.cache.EntityCache;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;

public class ServerEntitySetPassengersTranslator implements ModernToBetaHandler<ServerEntitySetPassengersPacket> {

    @Override
    public void translate(ServerEntitySetPassengersPacket packet, Session betaSession) {
        int entityId = packet.getEntityId();
        int[] passengers = packet.getPassengerIds();

        EntityCache cache = betaSession.getEntityCache();
        Entity vehicle = cache.getEntityById(entityId);

        if (vehicle == null) return;

        if (passengers.length != 0) {
            int inside = vehicle.getPassenger();
            if (passengers[0] != inside) {
                if (inside != -1) {
                    betaSession.sendPacket(new V1_7_3AttachEntityPacketData(inside, -1));
                    updateState(inside, false, -1, betaSession);
                }
                vehicle.setPassenger(passengers[0]);
                betaSession.sendPacket(new V1_7_3AttachEntityPacketData(passengers[0], vehicle.getEntityId()));
                updateState(passengers[0], true, vehicle.getEntityId(), betaSession);
            }
        } else {
            int old = vehicle.getPassenger();
            if (old != -1) {
                betaSession.sendPacket(new V1_7_3AttachEntityPacketData(old, -1));
                vehicle.setPassenger(-1);
                updateState(old, false, -1, betaSession);
            }
        }
        cache.addEntity(vehicle);
    }

    private void updateState(int playerId, boolean state, int vehicleId, Session session) {
        BetaPlayer player = session.getServer().getPlayer(playerId);
        if (player == null)
            return;

        if (!state && player.isSneaking()) {
            session.getModernClient().sendModernPacket(new ClientPlayerStatePacket(playerId, PlayerState.STOP_SNEAKING));
            player.setSneaking(false);
        }

        player.setInVehicle(state, vehicleId);
    }
}