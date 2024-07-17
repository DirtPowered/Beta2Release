package com.github.dirtpowered.betatorelease.network.codec;

import com.github.dirtpowered.betatorelease.Server;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class PipelineFactory extends ChannelInitializer<Channel> {

    private final Server server;

    public PipelineFactory(Server server) {
        this.server = server;
    }

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast("decoder", new PacketDecoder(server));
        channel.pipeline().addLast("encoder", new PacketEncoder(server));
    }
}