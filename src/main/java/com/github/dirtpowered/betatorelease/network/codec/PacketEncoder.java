package com.github.dirtpowered.betatorelease.network.codec;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.model.AbstractPacket;
import com.github.dirtpowered.betaprotocollib.model.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class PacketEncoder extends MessageToMessageEncoder<Object> {

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, List<Object> out) throws Exception {
        if (message instanceof Packet<?> packet) {
            AbstractPacket abstractPacket = BetaLib.getRegistry().getPacketInstance((Class<? extends AbstractPacket<?>>) packet.getPacketClass());

            ByteBuf packetId = Unpooled.buffer(1);
            packetId.writeByte(abstractPacket.getPacketId());

            out.add(Unpooled.wrappedBuffer(packetId, abstractPacket.writePacketData(packet)));
        }
        out.add(message);
    }
}