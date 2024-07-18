package com.github.dirtpowered.betatorelease.network.codec;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

public class PipelineFactory extends ChannelInitializer<Channel> {

    @Override
    protected void initChannel(Channel channel) {
        channel.pipeline().addLast("decoder", new PacketDecoder());
        channel.pipeline().addLast("encoder", new PacketEncoder());
    }
}