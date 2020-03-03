package com.github.dirtpowered.betatorelease.network.session;

import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betaprotocollib.packet.data.BlockChangePacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.ChatPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.KeepAlivePacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.KickDisconnectPacketData;
import com.github.dirtpowered.betaprotocollib.packet.data.MapChunkPacketData;
import com.github.dirtpowered.betatorelease.Server;
import com.github.dirtpowered.betatorelease.Utils.Tickable;
import com.github.dirtpowered.betatorelease.Utils.Utils;
import com.github.dirtpowered.betatorelease.data.chunk.BetaChunk;
import com.github.dirtpowered.betatorelease.data.chunk.Block;
import com.github.dirtpowered.betatorelease.model.ProtocolState;
import com.github.dirtpowered.betatorelease.network.handler.BetaToModernHandler;
import com.github.dirtpowered.betatorelease.network.registry.MessageHandlerRegistry;
import com.github.dirtpowered.betatorelease.network.registry.SessionRegistry;
import com.github.dirtpowered.betatorelease.proxy.ModernClient;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.pmw.tinylog.Logger;

import java.net.SocketAddress;
import java.util.Deque;
import java.util.LinkedList;

public class Session extends SimpleChannelInboundHandler<Packet> implements Tickable {
    private final Channel channel;
    private final SessionRegistry sessionRegistry;
    private final MessageHandlerRegistry messageHandlerRegistry;
    private final Deque<Block> blockQueue = new LinkedList<>();
    private final Deque<BetaChunk> chunkQueue = new LinkedList<>();
    private Server server;
    private ProtocolState protocolState;
    private int tickLimiter = 0;
    private ModernClient modernClient;
    private String playerName;
    private BetaPlayer betaPlayer;
    private boolean loggedIn;

    public Session(Server server, Channel channel, final SessionRegistry sessionRegistry, MessageHandlerRegistry messageHandlerRegistry) {
        this.server = server;
        this.channel = channel;
        this.sessionRegistry = sessionRegistry;
        this.messageHandlerRegistry = messageHandlerRegistry;
        this.protocolState = ProtocolState.HANDSHAKE;
        this.modernClient = new ModernClient(this);
        this.betaPlayer = new BetaPlayer(this);
    }

    @SuppressWarnings("unchecked")
    private void processPacket(Packet packet) {
        final BetaToModernHandler handler = messageHandlerRegistry.getMessageHandler(packet);

        if (handler != null) {
            handler.handlePacket(this, packet);
        } else {
            Logger.error("missing 'BetaToModern' translator for {}", packet.getClass().getSimpleName());
        }
    }

    public void sendPacket(Packet packet) {
        channel.writeAndFlush(packet);
    }

    public ProtocolState getProtocolState() {
        return protocolState;
    }

    public void setProtocolState(ProtocolState protocolState) {
        this.protocolState = protocolState;
    }

    @Override
    public void channelActive(final ChannelHandlerContext context) {
        sessionRegistry.addSession(this);

        Logger.info("new connection from ip {}", getAddress());
        Logger.info("session count: {}", sessionRegistry.getSessions().size());
    }

    @Override
    public void channelInactive(final ChannelHandlerContext context) {
        Logger.info("disconnected {}", getAddress());
        sessionRegistry.removeSession(this);
        context.close();
    }

    private SocketAddress getAddress() {
        return channel.remoteAddress();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        Logger.warn("closed connection: {} [{}]", cause.getLocalizedMessage(), getAddress());
        cause.printStackTrace();
        context.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Packet packet) {
        /* Server -> Client */
        processPacket(packet);
    }

    public Server getServer() {
        return server;
    }

    public ModernClient getModernClient() {
        return modernClient;
    }

    public void sendMessage(String message) {
        sendPacket(new ChatPacketData(message));
    }

    public void disconnect(String reason) {
        sendPacket(new KickDisconnectPacketData(reason));
    }

    @Override
    public void tick() {
        modernClient.tick();
        tickLimiter = (tickLimiter + 1) % 5; //24 chunks every 250ms
        if (tickLimiter == 0) {
            poolChunkQueue();
            poolBlockQueue();
        }
    }

    public void sendKeepAlive() {
        sendPacket(new KeepAlivePacketData());
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public BetaPlayer getBetaPlayer() {
        return betaPlayer;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void joinPlayer() {
    }

    private void poolBlockQueue() {
        if (blockQueue.isEmpty())
            return;

        int allowance = Math.min(64, blockQueue.size());

        for (int i = 0; i < allowance; i++) {
            Block block = blockQueue.remove();
            if (block == null)
                return;

            sendPacket(new BlockChangePacketData(
                    block.getX(),
                    block.getY(),
                    block.getZ(),
                    block.getBlockId(),
                    block.getBlockData()
            ));
        }
    }

    private void poolChunkQueue() {
        if (chunkQueue.isEmpty())
            return;

        int allowance = Math.min(24, chunkQueue.size());

        for (int i = 0; i < allowance; i++) {
            BetaChunk betaChunk = chunkQueue.remove();
            if (betaChunk == null)
                return;

            sendPacket(new MapChunkPacketData(
                    betaChunk.getX() * 16,
                    (short) 0,
                    betaChunk.getZ() * 16,
                    betaChunk.serializeTileData()
            ));
        }
    }

    public void queueBlock(Block block) {
        if (Utils.isBlockAllowed(block.getBlockId())) {
            blockQueue.add(block);
        } else {
            block.setBlockId(1);
            blockQueue.add(block);
        }
    }

    public void queueChunk(BetaChunk chunk) {
        chunkQueue.add(chunk);
    }
}
