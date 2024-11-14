package com.github.dirtpowered.betatorelease.network.handler;

import com.github.dirtpowered.betatorelease.Main;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@ChannelHandler.Sharable
public class ConnectionLimiterHandler extends ChannelInboundHandlerAdapter {
    private static final Cache<String, Long> CONNECTIONS = CacheBuilder.newBuilder()
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    private static final long THROTTLE_TIME_MS = 350L;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress isa = (InetSocketAddress) ctx.channel().remoteAddress();
        String address = isa.getAddress().getHostAddress();

        Long lastConn = CONNECTIONS.getIfPresent(address);

        if (lastConn != null && (System.currentTimeMillis() - lastConn < THROTTLE_TIME_MS)) {
            CONNECTIONS.put(address, System.currentTimeMillis());
            Main.LOGGER.info("Connection throttled: " + address);
            ctx.close();
            return;
        }

        CONNECTIONS.put(address, System.currentTimeMillis());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}