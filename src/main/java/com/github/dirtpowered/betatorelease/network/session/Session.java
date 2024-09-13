package com.github.dirtpowered.betatorelease.network.session;

import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.V1_7_3KickDisconnectPacketData;
import com.github.dirtpowered.betatorelease.Server;
import com.github.dirtpowered.betatorelease.data.entity.cache.EntityCache;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.network.registry.SessionRegistry;
import com.github.dirtpowered.betatorelease.proxy.ModernClient;
import com.github.dirtpowered.betatorelease.proxy.translator.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.BetaToModernRegistry;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.net.SocketAddress;

@Log4j2
public class Session extends SimpleChannelInboundHandler<Packet<?>> {
    private final Channel channel;
    private final SessionRegistry sessionRegistry;
    private final BetaToModernRegistry messageHandlerRegistry;

    @Getter
    private final Server server;

    @Getter
    private final EntityCache entityCache;

    @Getter
    @Setter
    private ProtocolState protocolState;

    @Getter
    private final ModernClient modernClient;

    @Setter
    @Getter
    private String playerName;

    @Getter
    private final BetaPlayer betaPlayer;

    @Setter
    @Getter
    private boolean loggedIn;

    public Session(Server server, Channel channel, final SessionRegistry sessionRegistry, BetaToModernRegistry betaToModernRegistry) {
        this.server = server;
        this.channel = channel;
        this.sessionRegistry = sessionRegistry;
        this.messageHandlerRegistry = betaToModernRegistry;
        this.protocolState = ProtocolState.HANDSHAKE;
        this.modernClient = new ModernClient(this);
        this.betaPlayer = new BetaPlayer(this);
        this.entityCache = new EntityCache();
    }

    @SuppressWarnings("unchecked")
    private void processPacket(Packet<?> packet) {
        final BetaToModernHandler handler = messageHandlerRegistry.getMessageHandler(packet);

        if (handler != null) {
            try {
                handler.handlePacket(this, packet);
            } catch (Exception e) {
                log.error("error while handling packet: {}", e.getMessage());
                e.printStackTrace();
            }
        } else {
            log.error("missing 'BetaToModern' translator for {}", packet.getClass().getSimpleName());
        }
    }

    public void sendPacket(Packet<?> packet) {
        this.channel.writeAndFlush(packet);
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) {
        this.sessionRegistry.addSession(this);

        log.info("new connection from ip {}", getAddress());
        log.info("session count: {}", sessionRegistry.getSessions().size());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext context) {
        log.info("disconnected {}", getAddress());
        this.sessionRegistry.removeSession(this);
        // disconnect from remote server too
        this.modernClient.disconnect("disconnected from remote server");
        context.close();
    }

    private SocketAddress getAddress() {
        return channel.remoteAddress();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        log.warn("closed connection: {} [{}]", cause.getLocalizedMessage(), getAddress());
        cause.printStackTrace();
        context.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        /* Server -> Client */
        processPacket(packet);
    }

    public void disconnect(String reason) {
        sendPacket(new V1_7_3KickDisconnectPacketData(reason));
    }

    public void joinPlayer() {
        // empty
    }
}