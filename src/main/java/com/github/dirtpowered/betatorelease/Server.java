package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betatorelease.configuration.Configuration;
import com.github.dirtpowered.betatorelease.data.entity.cache.PlayerCache;
import com.github.dirtpowered.betatorelease.network.codec.PipelineFactory;
import com.github.dirtpowered.betatorelease.network.registry.SessionRegistry;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.BetaToModernRegistry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.ModernToBetaRegistry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.TranslatorRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
public class Server {
    private final BetaToModernRegistry betaToModernRegistry;
    private final SessionRegistry sessionRegistry;

    @Getter
    private final ModernToBetaRegistry modernToBetaRegistry;

    @Getter
    private final Server server;

    @Getter
    private final PlayerCache playerCache;

    @Getter
    private final Configuration configuration;

    @Getter
    private final List<BetaPlayer> onlinePlayers = new ArrayList<>();

    private EventLoopGroup bossGroup;

    public Server() {
        this.server = this;
        this.betaToModernRegistry = new BetaToModernRegistry();
        this.sessionRegistry = new SessionRegistry();
        this.modernToBetaRegistry = new ModernToBetaRegistry();
        this.playerCache = new PlayerCache();
        this.configuration = new Configuration();

        // inject beta lib packets
        new TranslatorRegistry(betaToModernRegistry, modernToBetaRegistry).register();

        bind(configuration.getBindAddress(), configuration.getBindPort());
    }

    private void bind(String address, int port) {
        Class<? extends ServerChannel> socketChannel;

        if (Epoll.isAvailable()) {
            log.info("Epoll is available. Using it");
            this.bossGroup = new EpollEventLoopGroup();
            socketChannel = EpollServerSocketChannel.class;
        } else {
            log.warn("Epoll not available, using NIO. Reason: " + Epoll.unavailabilityCause().getMessage());
            this.bossGroup = new NioEventLoopGroup();
            socketChannel = NioServerSocketChannel.class;
        }

        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup)
                .channel(socketChannel)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast("mc_pipeline", new PipelineFactory());
                        channel.pipeline().addLast("user_session", new Session(getServer(), channel, sessionRegistry, betaToModernRegistry));
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture future = bootstrap.bind(address, port);
        future.addListener(f -> {
            if (!f.isSuccess()) {
                log.error("Failed to bind address: {}", f.cause().getLocalizedMessage());
            } else {
                log.info("Proxy is running on {}:{}", address, port);
            }
        });
    }

    protected void stop() {
        this.bossGroup.shutdownGracefully();
        // cleanup
        this.playerCache.getPlayers().clear();
        this.sessionRegistry.getSessions().clear();
    }

    public BetaPlayer getPlayer(int entityId) {
        return onlinePlayers.stream().filter(player -> player.getEntityId() == entityId).findFirst().orElse(null);
    }
}