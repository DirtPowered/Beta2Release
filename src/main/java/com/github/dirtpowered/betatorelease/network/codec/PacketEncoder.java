package com.github.dirtpowered.betatorelease.network.codec;

import com.github.dirtpowered.betaprotocollib.model.AbstractPacket;
import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betatorelease.Server;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.pmw.tinylog.Logger;

import java.util.List;

public class PacketEncoder extends MessageToMessageEncoder {

    @SuppressWarnings("unchecked")
    @Override
    protected void encode(ChannelHandlerContext ctx, Object message, List out) throws Exception {
        if (message instanceof Packet packet) {
            AbstractPacket abstractPacket = (AbstractPacket) packet.getPacketClass().getDeclaredConstructor().newInstance();

            ByteBuf packetId = Unpooled.buffer(1);
            packetId.writeByte(abstractPacket.getPacketId());

            out.add(Unpooled.wrappedBuffer(packetId, abstractPacket.writePacketData(packet)));
        }

        out.add(message);
    }
}