package com.github.dirtpowered.betatorelease.network.codec;

import com.github.dirtpowered.betaprotocollib.BetaLib;
import com.github.dirtpowered.betaprotocollib.model.AbstractPacket;
import com.github.dirtpowered.betaprotocollib.model.Packet;
import com.github.dirtpowered.betatorelease.Server;
import com.github.dirtpowered.betatorelease.utils.Utils;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class PacketDecoder extends ReplayingDecoder<Packet<?>> {
    private final Server server;

    PacketDecoder(Server server) {
        this.server = server;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buffer, List<Object> list) throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        final int packetId = buffer.readUnsignedByte();

        if (!BetaLib.getRegistry().hasId(packetId)) {
            Logger.warn("Packet {}[{}] is not registered", Utils.toHex(packetId), packetId);
            list.add(Unpooled.EMPTY_BUFFER);
            return;
        }

        if (BetaLib.getRegistry().getFromId(packetId) == null)
            return;

        AbstractPacket<?> abstractPacket = BetaLib.getRegistry().getFromId(packetId).getDeclaredConstructor().newInstance();

        Object o = abstractPacket.readPacketData(buffer);
        if (server.isDebugMode()) {
            Logger.info("received packet: {}", ToStringBuilder.reflectionToString(o));
        }
        list.add(o);
    }
}