package com.github.dirtpowered.betatorelease.proxy.translator.clientbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.AttachEntityPacketData;
import com.github.dirtpowered.betatorelease.utils.Utils;
import com.github.dirtpowered.betatorelease.data.entity.EntityVehicle;
import com.github.dirtpowered.betatorelease.data.entity.cache.EntityCache;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;

public class ServerEntitySetPassengersTranslator implements ModernToBetaHandler<ServerEntitySetPassengersPacket> {

    @Override
    public void translate(ServerEntitySetPassengersPacket packet, Session betaSession) {
        //https://wiki.vg/index.php?title=Protocol&oldid=689#Attach_Entity_.280x27.29
        //NOTE: It supports only one passenger.

        int vehicleEntityId = packet.getEntityId();
        int[] passengerEntityIds = packet.getPassengerIds();
        EntityCache cache = betaSession.getServer().getEntityCache();

        try {
            EntityVehicle vehicle = new EntityVehicle(vehicleEntityId);
            vehicle.setPassenger(passengerEntityIds[0]);
            BetaPlayer player = betaSession.getServer().getPlayer(passengerEntityIds[0]);
            if (player != null)
                player.setInVehicle(true, vehicleEntityId);

            cache.addEntity(vehicle);
            betaSession.sendPacket(new AttachEntityPacketData(passengerEntityIds[0], vehicleEntityId));
        } catch (ArrayIndexOutOfBoundsException e) {
            EntityVehicle vehicle = ((EntityVehicle) cache.getEntityById(vehicleEntityId));
            if (vehicle != null) {
                BetaPlayer player = betaSession.getServer().getPlayer(vehicle.getPassenger());

                if (player != null)
                    player.setInVehicle(false, -1);
                betaSession.sendPacket(new AttachEntityPacketData(vehicle.getPassenger(), -1));
                cache.removeEntity(vehicle.getEntityId());
            }
        }

        Utils.debug(packet);
    }
}
