package com.github.dirtpowered.betatorelease.network.handler;

import com.github.dirtpowered.betatorelease.Main;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;

public class VersionDetectionHandler extends ChannelInboundHandlerAdapter {
    public final static AttributeKey<String> PROTOCOL_ATTRIBUTE = AttributeKey.valueOf("protocol");

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object object) {
        ByteBuf buffer = (ByteBuf) object;
        try {
            short packetId = buffer.readUnsignedByte();
            ctx.channel().attr(PROTOCOL_ATTRIBUTE).set(packetId != 0x02 && packetId != 0xFE ? "modern" : "legacy");
        } catch (Exception e) {
            Main.LOGGER.error("Error while reading packet id", e);
        } finally {
            buffer.resetReaderIndex();

            ctx.channel().pipeline().remove(this);
            ctx.fireChannelRead(object);
        }
    }
}