package com.github.dirtpowered.betatorelease;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.packet.data.AnimationPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.BlockDigPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.BlockItemSwitchPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.BlockPlacePacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.ChatPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.CloseWindowPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.EntityActionPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.FlyingPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.HandshakePacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.KeepAlivePacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.KickDisconnectPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.LoginPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.PlayerLookMovePacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.PlayerLookPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.PlayerPositionPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.RespawnPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.TransactionPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.UpdateHealthPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.UseEntityPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.WindowClickPacketData;
import com.github.dirtpowered.betatorelease.configuration.Configuration;
import com.github.dirtpowered.betatorelease.data.entity.cache.EntityCache;
import com.github.dirtpowered.betatorelease.data.entity.cache.PlayerCache;
import com.github.dirtpowered.betatorelease.network.codec.PipelineFactory;
import com.github.dirtpowered.betatorelease.network.handler.connection.AnimationPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.BlockDigPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.BlockItemSwitchPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.BlockPlacePacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.ChatPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.CloseWindowPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.EntityActionPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.FlyingPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.HandshakePacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.KeepAlivePacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.KickDisconnectPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.LoginPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.PlayerLookMovePacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.PlayerLookPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.PlayerPositionPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.RespawnPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.TransactionPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.UpdateHealthPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.UseEntityPacketHandler;
import com.github.dirtpowered.betatorelease.network.handler.connection.WindowClickPacketHandler;
import com.github.dirtpowered.betatorelease.network.registry.MessageHandlerRegistry;
import com.github.dirtpowered.betatorelease.network.registry.SessionRegistry;
import com.github.dirtpowered.betatorelease.network.session.BetaPlayer;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.LoginDisconnectTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerBlockChangeTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerChatTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerChunkDataTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerConfirmTransactionTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerDisconnectTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityAnimationTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityCollectItemTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityDestroyTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityEquipmentTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityHeadLookTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityMetadataTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityPositionRotationTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityPositionTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityRotationTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntitySetPassengersTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityStatusTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityTeleportTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerEntityVelocityTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerExplosionTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerJoinGameTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerKeepAliveTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerMultiBlockChangeTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerNotifyClientTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerOpenWindowTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerPlayBuiltinSoundTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerPlayerChangeHeldItemTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerPlayerHealthTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerPlayerListEntryTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerPlayerPositionRotationTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerRespawnTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerSetSlotTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerSpawnGlobalEntityTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerSpawnMobTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerSpawnObjectTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerSpawnPlayerTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerSpawnPositionTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerUnloadChunkTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerUpdateTileEntityTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerVehicleMoveTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.ServerWindowItemsTranslator;
import com.github.dirtpowered.betatorelease.proxy.translator.moderntranslators.UpdateTimeTranslator;
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
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnGlobalEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnMobPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnObjectPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerConfirmTransactionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerOpenWindowPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerWindowItemsPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerExplosionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerMultiBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerPlayBuiltinSoundPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTileEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;
import com.github.steveice10.mc.protocol.packet.login.server.LoginDisconnectPacket;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server implements Runnable {
    private final MessageHandlerRegistry messageHandlerRegistry;
    private final SessionRegistry sessionRegistry;
    private final TranslatorRegistry translatorRegistry;
    private final ScheduledExecutorService scheduledExecutorService;
    private EventLoopGroup bossGroup = new NioEventLoopGroup();
    private EventLoopGroup workerGroup = new NioEventLoopGroup();
    private Server server;
    private EntityCache entityCache;
    private PlayerCache playerCache;
    private Configuration configuration;
    private List<BetaPlayer> onlinePlayers = new ArrayList<>();

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

        //register packets
        BetaLib.inject();

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

    public Configuration getConfiguration() {
        return configuration;
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

    public TranslatorRegistry getTranslatorRegistry() {
        return translatorRegistry;
    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    private void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public EntityCache getEntityCache() {
        return entityCache;
    }

    public PlayerCache getPlayerCache() {
        return playerCache;
    }

    public List<BetaPlayer> getOnlinePlayers() {
        return onlinePlayers;
    }

    public BetaPlayer getPlayer(int entityId) {
        return onlinePlayers.stream().filter(player -> player.getEntityId() == entityId).findFirst().orElse(null);
    }
}
