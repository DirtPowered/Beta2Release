package com.github.dirtpowered.betatorelease.proxy.session;

import com.github.steveice10.packetlib.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HAProxySessionFactory implements SessionFactory {
    private final com.github.dirtpowered.betatorelease.network.session.Session session;

    @Override
    public Session createClientSession(Client client) {
        return new HAProxyClientSession(client, session);
    }

    @Override
    public ConnectionListener createServerListener(Server server) {
        return null;
    }
}