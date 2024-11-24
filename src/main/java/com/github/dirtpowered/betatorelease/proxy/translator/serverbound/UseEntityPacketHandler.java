package com.github.dirtpowered.betatorelease.proxy.translator.serverbound;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3UseEntityPacketData;
import com.github.dirtpowered.betatorelease.data.entity.EntityVehicle;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.data.game.entity.player.PlayerState;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerStatePacket;

public class UseEntityPacketHandler implements BetaToModernHandler<V1_7_3UseEntityPacketData> {

    @Override
    public void handlePacket(Session session, V1_7_3UseEntityPacketData packetClass) {
        int target = packetClass.getTargetEntityId();
        boolean leftClick = packetClass.isLeftClick();

        InteractAction action = leftClick ? InteractAction.ATTACK : InteractAction.INTERACT_AT;
        Entity entity = session.getEntityCache().getEntityById(packetClass.getTargetEntityId());

        if ((entity instanceof EntityVehicle || (entity != null && entity.getMobType() != null)) && !leftClick) {
            action = InteractAction.INTERACT;

            BetaPlayer player = session.getBetaPlayer();

            // fix vehicle eject
            if (player.isInVehicle() && !player.isSneaking()) {
                session.getModernClient().sendModernPacket(new ClientPlayerStatePacket(player.getEntityId(), PlayerState.START_SNEAKING));
                player.setSneaking(true);
            }
        }

        session.getModernClient().sendModernPacket(new ClientPlayerInteractEntityPacket(target, action));
    }
}