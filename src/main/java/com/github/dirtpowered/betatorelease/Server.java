package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import com.github.dirtpowered.betatorelease.configuration.Configuration;
import com.github.dirtpowered.betatorelease.data.entity.cache.PlayerCache;
import com.github.dirtpowered.betatorelease.network.codec.PipelineFactory;
import com.github.dirtpowered.betatorelease.network.registry.SessionRegistry;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.BetaToModernRegistry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.ModernToBetaRegistry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.b17.B17Registry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.b18.B18Registry;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.b19.B19Registry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.Getter;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private final BetaToModernRegistry betaToModernRegistry;
    private final SessionRegistry sessionRegistry;

    @Getter
    private final ModernToBetaRegistry modernToBetaRegistry;

    private final Server server;

    @Getter
    private final PlayerCache playerCache;

    @Getter
    private final Configuration configuration;

    @Getter
    private final List<BetaPlayer> onlinePlayers = new ArrayList<>();

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @Getter
    private final MinecraftVersion version;

    public Server() {
        this.server = this;
        this.betaToModernRegistry = new BetaToModernRegistry();
        this.sessionRegistry = new SessionRegistry();
        this.modernToBetaRegistry = new ModernToBetaRegistry();
        this.playerCache = new PlayerCache();
        this.configuration = new Configuration();
        this.version = configuration.getVersion();

        // inject beta lib packets
        switch (version) {
            case B_1_6_6, B_1_7_3 -> new B17Registry(betaToModernRegistry, modernToBetaRegistry).register();
            case B_1_8_1 -> new B18Registry(betaToModernRegistry, modernToBetaRegistry).register();
            case B_1_9 -> new B19Registry(betaToModernRegistry, modernToBetaRegistry).register();
        }

        bind(configuration.getBindAddress(), configuration.getBindPort());
    }

    private void bind(String address, int port) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast("mc_pipeline", new PipelineFactory());
                        channel.pipeline().addLast("user_session", new Session(getServer(), channel, sessionRegistry, betaToModernRegistry));
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture f;
        try {
            Logger.info("Ready for connections!");

            f = b.bind(address, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Logger.error("Address in use: {}", e.getLocalizedMessage());
        }
    }

    void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        playerCache.getPlayers().clear();
        sessionRegistry.getSessions().clear();
    }

    private Server getServer() {
        return server;
    }

    public BetaPlayer getPlayer(int entityId) {
        return onlinePlayers.stream().filter(player -> player.getEntityId() == entityId).findFirst().orElse(null);
    }
}