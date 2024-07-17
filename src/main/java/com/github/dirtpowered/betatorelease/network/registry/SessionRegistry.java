package com.github.dirtpowered.betatorelease.network.registry;

import com.github.dirtpowered.betatorelease.network.session.Session;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SessionRegistry {
    private final Set<Session> sessions = ConcurrentHashMap.newKeySet();

    public void addSession(final Session session) {
        this.sessions.add(session);
    }

    public void removeSession(final Session session) {
        this.sessions.remove(session);
    }

    public Set<Session> getSessions() {
        return Collections.unmodifiableSet(sessions);
    }
}