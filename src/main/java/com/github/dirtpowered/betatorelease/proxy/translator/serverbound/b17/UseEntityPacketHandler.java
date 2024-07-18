package com.github.dirtpowered.betatorelease.proxy.translator.serverbound.b17;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UseEntityPacketData;
import com.github.dirtpowered.betatorelease.data.entity.EntityVehicle;
import com.github.dirtpowered.betatorelease.data.entity.model.Entity;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.steveice10.mc.protocol.data.game.entity.player.InteractAction;
import com.github.steveice10.mc.protocol.packet.ingame.client.player.ClientPlayerInteractEntityPacket;

public class UseEntityPacketHandler implements BetaToModernHandler<UseEntityPacketData> {

    @Override
    public void handlePacket(Session session, UseEntityPacketData packetClass) {
        int target = packetClass.getTargetEntityId();
        boolean leftClick = packetClass.isLeftClick();

        InteractAction action = leftClick ? InteractAction.ATTACK : InteractAction.INTERACT_AT;
        Entity entity = session.getEntityCache().getEntityById(packetClass.getTargetEntityId());

        if (entity instanceof EntityVehicle && !leftClick)
            action = InteractAction.INTERACT;

        session.getModernClient().sendModernPacket(new ClientPlayerInteractEntityPacket(target, action));
    }
}