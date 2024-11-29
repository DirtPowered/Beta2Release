package com.github.dirtpowered.betatorelease.network.codec;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.model.AbstractPacket;
import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.io.IOException;
import java.util.List;

public class PacketDecoder extends ReplayingDecoder<Packet<?>> {

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws IOException {
        int packetId = buffer.readUnsignedByte();

        if (!BetaLib.getRegistry().hasPacket(packetId)) {
            Main.LOGGER.warn("Packet {}[{}] is not registered", Utils.toHex(packetId), packetId);
            return;
        }

        AbstractPacket<?> abstractPacket = BetaLib.getRegistry().createOrGetInstance(packetId);
        Object o = abstractPacket.readPacketData(buffer);
        list.add(o);
    }
}