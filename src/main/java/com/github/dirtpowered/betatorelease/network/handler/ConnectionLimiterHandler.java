package com.github.dirtpowered.betatorelease.network.handler;

import com.github.dirtpowered.betatorelease.Main;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

@ChannelHandler.Sharable
public class ConnectionLimiterHandler extends ChannelInboundHandlerAdapter {
    private static final Map<String, Long> CONNECTIONS = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        InetSocketAddress isa = (InetSocketAddress) ctx.channel().remoteAddress();
        String address = isa.getAddress().getHostAddress();

        if (CONNECTIONS.containsKey(address)) {
            if (System.currentTimeMillis() - CONNECTIONS.get(address) < 350L) {
                CONNECTIONS.put(address, System.currentTimeMillis());
                Main.LOGGER.info("Connection throttled: " + address);
                ctx.close();
                return;
            }
        }

        CONNECTIONS.put(address, System.currentTimeMillis());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }
}