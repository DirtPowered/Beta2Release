package com.github.dirtpowered.betatorelease.proxy.session;

import com.github.dirtpowered.betatorelease.Main;
import com.github.dirtpowered.betatorelease.network.session.Session;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.tcp.TcpPacketCodec;
import com.github.steveice10.packetlib.tcp.TcpPacketEncryptor;
import com.github.steveice10.packetlib.tcp.TcpPacketSizer;
import com.github.steveice10.packetlib.tcp.TcpSession;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.haproxy.*;

import javax.naming.directory.InitialDirContext;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.util.Hashtable;

public class HAProxyClientSession extends TcpSession {
    private static final String ENCODER_HEADER = "haproxy-header";
    private static final String ENCODER_NAME = "haproxy-encoder";
    private final Client client;
    private final Session session;

    public HAProxyClientSession(Client client, Session session) {
        super(client.getHost(), client.getPort(), client.getPacketProtocol());
        this.client = client;
        this.session = session;
    }

    @Override
    public void connect(boolean wait) {
        try {
            EventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);

            bootstrap.handler(new ChannelInitializer<>() {
                @Override
                public void initChannel(Channel channel) {
                    getPacketProtocol().newClientSession(client, HAProxyClientSession.this);

                    channel.config().setOption(ChannelOption.IP_TOS, 0x18);
                    channel.config().setOption(ChannelOption.TCP_NODELAY, false);

                    ChannelPipeline pipeline = channel.pipeline();

                    refreshReadTimeoutHandler(channel);
                    refreshWriteTimeoutHandler(channel);

                    pipeline.addLast("encryption", new TcpPacketEncryptor(HAProxyClientSession.this));
                    pipeline.addLast("sizer", new TcpPacketSizer(HAProxyClientSession.this));
                    pipeline.addLast("codec", new TcpPacketCodec(HAProxyClientSession.this));
                    pipeline.addLast("manager", HAProxyClientSession.this);

                    addHAProxySupport(pipeline);
                }
            });
            bootstrap.group(group);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout() * 1000);

            resolveAddress(bootstrap);

            ChannelFuture future = bootstrap.connect();
            if (wait) future.sync();

            future.addListener((futureListener) -> {
                if (!futureListener.isSuccess()) {
                    exceptionCaught(null, futureListener.cause());
                }
            });
        } catch (Throwable throwable) {
            exceptionCaught(null, throwable);
        }
    }

    private void addHAProxySupport(ChannelPipeline pipeline) {
        InetSocketAddress addr = (InetSocketAddress) session.getAddress();
        pipeline.addFirst(ENCODER_HEADER, new ChannelInboundHandlerAdapter() {
            @Override
            public void channelActive(ChannelHandlerContext ctx) throws Exception {
                HAProxyProxiedProtocol protocol = addr.getAddress() instanceof Inet4Address ? HAProxyProxiedProtocol.TCP4 : HAProxyProxiedProtocol.TCP6;
                InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();

                ctx.channel().writeAndFlush(new HAProxyMessage(HAProxyProtocolVersion.V2, HAProxyCommand.PROXY, protocol,
                        addr.getAddress().getHostAddress(), remoteAddress.getAddress().getHostAddress(),
                        addr.getPort(), remoteAddress.getPort()
                ));
                ctx.pipeline().remove(this);
                ctx.pipeline().remove(ENCODER_NAME);
                super.channelActive(ctx);
            }
        });
        pipeline.addFirst(ENCODER_NAME, HAProxyMessageEncoder.INSTANCE);
    }

    private void resolveAddress(Bootstrap bootstrap) {
        String host = getHost();
        int port = getPort();

        try {
            Hashtable<String, String> environment = new Hashtable<>();
            environment.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            environment.put("java.naming.provider.url", "dns:");

            String[] result = new InitialDirContext(environment).getAttributes(getPacketProtocol().getSRVRecordPrefix() + "._tcp." + host, new String[]{"SRV"}).get("srv").get().toString().split(" ", 4);
            host = result[3];
            port = Integer.parseInt(result[2]);
        } catch (Throwable t) {
            Main.LOGGER.debug("Failed to resolve SRV record for {}: {}", host, t.getMessage());
        }
        bootstrap.remoteAddress(host, port);
    }
}