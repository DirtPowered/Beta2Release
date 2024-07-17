package com.github.dirtpowered.betatorelease.proxy.translator;

import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betatorelease.network.session.Session;

public interface BetaToModernHandler<T extends Packet<?>> {

    void handlePacket(final Session session, final T packetClass);
}