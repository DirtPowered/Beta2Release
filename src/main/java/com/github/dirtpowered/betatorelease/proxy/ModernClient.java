package com.github.dirtpowered.betatorelease.proxy;

import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.Server;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.session.HAProxySessionFactory;
import com.github.dirtpowered.betatorelease.proxy.translator.ModernToBetaHandler;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.SessionFactory;
import com.github.steveice10.packetlib.event.session.ConnectedEvent;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.packet.Packet;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

public class ModernClient {
    private Client client;
    private final Session betaSession;
    private final Server server;

    public ModernClient(Session betaSession) {
        this.betaSession = betaSession;
        this.server = betaSession.getServer();
    }

    public void connect(Session session) {
        MinecraftProtocol protocol = new MinecraftProtocol(betaSession.getPlayerName());
        SessionFactory sessionFactory = getSessionFactory(session);

        client = new Client(server.getConfiguration().getRemoteAddress(), server.getConfiguration().getRemotePort(), protocol, sessionFactory);
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                /* Modern -> Beta */
                processPacket(event.getPacket());
            }

            @Override
            public void connected(ConnectedEvent event) {
                server.getOnlinePlayers().add(betaSession.getBetaPlayer());
                int online = betaSession.getServer().getOnlinePlayers().size();

                Main.LOGGER.info("{} ({}) connected to remote server [{}]", betaSession.getPlayerName(), betaSession.getAddress(), online);
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                server.getOnlinePlayers().remove(betaSession.getBetaPlayer());
                int online = betaSession.getServer().getOnlinePlayers().size();

                client.getSession().disconnect(event.getReason());
                betaSession.disconnect(event.getReason());
                Main.LOGGER.info("{} ({}) disconnected from remote server, reason: {} [{}]", betaSession.getPlayerName(), betaSession.getAddress(), event.getReason(), online);
            }
        });
        client.getSession().connect();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void processPacket(Packet packet) {
        ModernToBetaHandler handler = betaSession.getServer().getModernToBetaRegistry().getByPacket(packet);
        if (handler == null)
            return;

        try {
            handler.translate(packet, betaSession);
        } catch (Exception e) {
            Main.LOGGER.error("Error while translating packet: {}", packet.getClass().getSimpleName(), e);
        }
    }

    public void disconnect(String reason) {
        if (checkConnection()) return;

        this.client.getSession().disconnect(reason);
        this.betaSession.disconnect(reason);
    }

    public void sendModernPacket(Packet modernPacket) {
        if (checkConnection()) return;
        this.client.getSession().send(modernPacket);
    }

    private SessionFactory getSessionFactory(Session session) {
        SessionFactory sessionFactory;
        if (server.getConfiguration().isHaproxySupport()) {
            sessionFactory = new HAProxySessionFactory(session);
        } else {
            sessionFactory = new TcpSessionFactory();
        }
        return sessionFactory;
    }

    private boolean checkConnection() {
        return client == null || !client.getSession().isConnected();
    }
}