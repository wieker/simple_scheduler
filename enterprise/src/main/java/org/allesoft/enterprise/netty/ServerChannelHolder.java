package org.allesoft.enterprise.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.springframework.beans.factory.InitializingBean;

public class ServerChannelHolder implements InitializingBean {
    private final ServerBootstrap serverBootstrap;
    private Integer tcpPort;
    private Channel serverChannel;

    public ServerChannelHolder(ServerBootstrap serverBootstrap, Integer tcpPort) {
        this.serverBootstrap = serverBootstrap;
        this.tcpPort = tcpPort;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        startup();
    }

    private void startup() throws InterruptedException {
        ChannelFuture serverChannelFuture = serverBootstrap.bind(tcpPort).sync();
        serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
    }
}
