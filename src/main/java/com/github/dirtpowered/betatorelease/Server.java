package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.data.version.MinecraftVersion;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.*;
import com.github.dirtpowered.betatorelease.configuration.Configuration;
import com.github.dirtpowered.betatorelease.data.entity.cache.EntityCache;
import com.github.dirtpowered.betatorelease.data.entity.cache.PlayerCache;
import com.github.dirtpowered.betatorelease.network.codec.PipelineFactory;
import com.github.dirtpowered.betatorelease.network.handler.connection.*;
import com.github.dirtpowered.betatorelease.network.registry.MessageHandlerRegistry;
import com.github.dirtpowered.betatorelease.network.registry.SessionRegistry;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.*;
import com.github.dirtpowered.betatorelease.proxy.translator.registry.TranslatorRegistry;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDisconnectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerKeepAlivePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityCollectItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityEquipmentPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityHeadLookPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntitySetPassengersPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerVehicleMovePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerChangeHeldItemPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerUseBedPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPaintingPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerCloseWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowPropertyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.*;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    private final MessageHandlerRegistry messageHandlerRegistry;
    private final SessionRegistry sessionRegistry;

    @Getter
    private final TranslatorRegistry translatorRegistry;

    @Getter
    private final ScheduledExecutorService scheduledExecutorService;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();
    private final Server server;

    @Getter
    private final EntityCache entityCache;

    @Getter
    private final PlayerCache playerCache;

    @Getter
    private final Configuration configuration;

    @Getter
    private final List<BetaPlayer> onlinePlayers = new ArrayList<>();

    @Getter
    private boolean debugMode;

    Server() {
        this.server = this;
        this.messageHandlerRegistry = new MessageHandlerRegistry();
        this.sessionRegistry = new SessionRegistry();
        this.translatorRegistry = new TranslatorRegistry();
        this.scheduledExecutorService = Executors.newScheduledThreadPool(32);
        this.entityCache = new EntityCache();
        this.playerCache = new PlayerCache();
        this.configuration = new Configuration();

        setDebugMode(false); //TODO: Configuration?

        //register packet handlers
        messageHandlerRegistry.registerHandler(HandshakePacketData.class, new HandshakePacketHandler());
        messageHandlerRegistry.registerHandler(LoginPacketData.class, new LoginPacketHandler());
        messageHandlerRegistry.registerHandler(PlayerLookMovePacketData.class, new PlayerLookMovePacketHandler());
        messageHandlerRegistry.registerHandler(PlayerPositionPacketData.class, new PlayerPositionPacketHandler());
        messageHandlerRegistry.registerHandler(KeepAlivePacketData.class, new KeepAlivePacketHandler());
        messageHandlerRegistry.registerHandler(ChatPacketData.class, new ChatPacketHandler());
        messageHandlerRegistry.registerHandler(UpdateHealthPacketData.class, new UpdateHealthPacketHandler());
        messageHandlerRegistry.registerHandler(KickDisconnectPacketData.class, new KickDisconnectPacketHandler());
        messageHandlerRegistry.registerHandler(RespawnPacketData.class, new RespawnPacketHandler());
        messageHandlerRegistry.registerHandler(AnimationPacketData.class, new AnimationPacketHandler());
        messageHandlerRegistry.registerHandler(EntityActionPacketData.class, new EntityActionPacketHandler());
        messageHandlerRegistry.registerHandler(BlockItemSwitchPacketData.class, new BlockItemSwitchPacketHandler());
        messageHandlerRegistry.registerHandler(BlockDigPacketData.class, new BlockDigPacketHandler());
        messageHandlerRegistry.registerHandler(FlyingPacketData.class, new FlyingPacketHandler());
        messageHandlerRegistry.registerHandler(PlayerLookPacketData.class, new PlayerLookPacketHandler());
        messageHandlerRegistry.registerHandler(BlockPlacePacketData.class, new BlockPlacePacketHandler());
        messageHandlerRegistry.registerHandler(CloseWindowPacketData.class, new CloseWindowPacketHandler());
        messageHandlerRegistry.registerHandler(WindowClickPacketData.class, new WindowClickPacketHandler());
        messageHandlerRegistry.registerHandler(TransactionPacketData.class, new TransactionPacketHandler());
        messageHandlerRegistry.registerHandler(UseEntityPacketData.class, new UseEntityPacketHandler());
        messageHandlerRegistry.registerHandler(UpdateSignPacketData.class, new UpdateSignPacketHandler());

        //register packets
        BetaLib.inject(MinecraftVersion.B_1_7_3);

        //register translators
        translatorRegistry.registerTranslator(ServerChatPacket.class, new ServerChatTranslator());
        translatorRegistry.registerTranslator(ServerUpdateTimePacket.class, new UpdateTimeTranslator());
        translatorRegistry.registerTranslator(ServerPlayerHealthPacket.class, new ServerPlayerHealthTranslator());
        translatorRegistry.registerTranslator(ServerRespawnPacket.class, new ServerRespawnTranslator());
        translatorRegistry.registerTranslator(ServerChunkDataPacket.class, new ServerChunkDataTranslator());
        translatorRegistry.registerTranslator(ServerSpawnMobPacket.class, new ServerSpawnMobTranslator());
        translatorRegistry.registerTranslator(ServerJoinGamePacket.class, new ServerJoinGameTranslator());
        translatorRegistry.registerTranslator(ServerSpawnPlayerPacket.class, new ServerSpawnPlayerTranslator());
        translatorRegistry.registerTranslator(ServerPlayerPositionRotationPacket.class, new ServerPlayerPositionRotationTranslator());
        translatorRegistry.registerTranslator(ServerBlockChangePacket.class, new ServerBlockChangeTranslator());
        translatorRegistry.registerTranslator(ServerNotifyClientPacket.class, new ServerNotifyClientTranslator());
        translatorRegistry.registerTranslator(ServerEntityHeadLookPacket.class, new ServerEntityHeadLookTranslator());
        translatorRegistry.registerTranslator(ServerSetSlotPacket.class, new ServerSetSlotTranslator());
        translatorRegistry.registerTranslator(ServerEntityVelocityPacket.class, new ServerEntityVelocityTranslator());
        translatorRegistry.registerTranslator(ServerUnloadChunkPacket.class, new ServerUnloadChunkTranslator());
        translatorRegistry.registerTranslator(ServerMultiBlockChangePacket.class, new ServerMultiBlockChangeTranslator());
        translatorRegistry.registerTranslator(ServerSpawnPositionPacket.class, new ServerSpawnPositionTranslator());
        translatorRegistry.registerTranslator(ServerEntityTeleportPacket.class, new ServerEntityTeleportTranslator());
        translatorRegistry.registerTranslator(ServerEntityPositionPacket.class, new ServerEntityPositionTranslator());
        translatorRegistry.registerTranslator(ServerEntityDestroyPacket.class, new ServerEntityDestroyTranslator());
        translatorRegistry.registerTranslator(ServerEntityAnimationPacket.class, new ServerEntityAnimationTranslator());
        translatorRegistry.registerTranslator(ServerPlayerChangeHeldItemPacket.class, new ServerPlayerChangeHeldItemTranslator());
        translatorRegistry.registerTranslator(ServerEntityPositionRotationPacket.class, new ServerEntityPositionRotationTranslator());
        translatorRegistry.registerTranslator(ServerEntityRotationPacket.class, new ServerEntityRotationTranslator());
        translatorRegistry.registerTranslator(ServerKeepAlivePacket.class, new ServerKeepAliveTranslator());
        translatorRegistry.registerTranslator(LoginDisconnectPacket.class, new LoginDisconnectTranslator());
        translatorRegistry.registerTranslator(ServerEntityStatusPacket.class, new ServerEntityStatusTranslator());
        translatorRegistry.registerTranslator(ServerSpawnGlobalEntityPacket.class, new ServerSpawnGlobalEntityTranslator());
        translatorRegistry.registerTranslator(ServerExplosionPacket.class, new ServerExplosionTranslator());
        translatorRegistry.registerTranslator(ServerOpenWindowPacket.class, new ServerOpenWindowTranslator());
        translatorRegistry.registerTranslator(ServerWindowItemsPacket.class, new ServerWindowItemsTranslator());
        translatorRegistry.registerTranslator(ServerConfirmTransactionPacket.class, new ServerConfirmTransactionTranslator());
        translatorRegistry.registerTranslator(ServerSpawnObjectPacket.class, new ServerSpawnObjectTranslator());
        translatorRegistry.registerTranslator(ServerEntityCollectItemPacket.class, new ServerEntityCollectItemTranslator());
        translatorRegistry.registerTranslator(ServerEntityMetadataPacket.class, new ServerEntityMetadataTranslator());
        translatorRegistry.registerTranslator(ServerUpdateTileEntityPacket.class, new ServerUpdateTileEntityTranslator());
        translatorRegistry.registerTranslator(ServerDisconnectPacket.class, new ServerDisconnectTranslator());
        translatorRegistry.registerTranslator(ServerEntityEquipmentPacket.class, new ServerEntityEquipmentTranslator());
        translatorRegistry.registerTranslator(ServerPlayerListEntryPacket.class, new ServerPlayerListEntryTranslator());
        translatorRegistry.registerTranslator(ServerEntitySetPassengersPacket.class, new ServerEntitySetPassengersTranslator());
        translatorRegistry.registerTranslator(ServerPlayBuiltinSoundPacket.class, new ServerPlayBuiltinSoundTranslator());
        translatorRegistry.registerTranslator(ServerVehicleMovePacket.class, new ServerVehicleMoveTranslator());
        translatorRegistry.registerTranslator(ServerPlayerUseBedPacket.class, new ServerPlayerUseBedTranslator());
        translatorRegistry.registerTranslator(ServerSpawnPaintingPacket.class, new ServerSpawnPaintingTranslator());
        translatorRegistry.registerTranslator(ServerCloseWindowPacket.class, new ServerCloseWindowTranslator());
        translatorRegistry.registerTranslator(ServerWindowPropertyPacket.class, new ServerWindowPropertyTranslator());
        translatorRegistry.registerTranslator(ServerPlayEffectPacket.class, new ServerPlayEffectTranslator());
        translatorRegistry.registerTranslator(ServerMapDataPacket.class, new ServerMapDataTranslator());

        //global tick loop
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> new Thread(r, "Server Thread"));
        executor.scheduleAtFixedRate(this, 0L, 50L, TimeUnit.MILLISECONDS);
        bind(configuration.getBindAdress(), configuration.getBindPort());
    }

    @Override
    public void run() {
        for (final Session session : sessionRegistry.getSessions()) {
            session.tick();
        }
    }

    private void bind(String addr, int port) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) {
                        channel.pipeline().addLast("mc_pipeline", new PipelineFactory(getServer()));
                        channel.pipeline().addLast("user_session", new Session(getServer(), channel, sessionRegistry, messageHandlerRegistry));
                    }
                })
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture f;
        try {
            Logger.info("Ready for connections!");

            f = b.bind(addr, port).sync();
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Logger.error("Address in use: {}", e.getLocalizedMessage());
        }
    }

    void stop() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        entityCache.getEntities().clear();
        playerCache.getPlayers().clear();
        sessionRegistry.getSessions().clear();
    }

    private Server getServer() {
        return server;
    }

    private void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public BetaPlayer getPlayer(int entityId) {
        return onlinePlayers.stream().filter(player -> player.getEntityId() == entityId).findFirst().orElse(null);
    }
}