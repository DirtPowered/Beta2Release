package com.github.dirtpowered.betatorelease.proxy.translator;

import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.packetlib.packet.Packet;

public interface ModernToBetaHandler<T extends Packet> {

    void translate(T packet, Session betaSession);
}