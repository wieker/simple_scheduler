package org.allesoft.enterprise.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringConfig implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    public ServerBootstrap serverBootstrap(EventLoopGroup bossGroup, EventLoopGroup workerGroup, ChannelInitializer<SocketChannel> childHandler) {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(childHandler)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        return b;
    }

    public ChannelInitializer<SocketChannel> channelInit() {
        return new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast((io.netty.channel.ChannelHandler) applicationContext.getBean("channelHandler"));
            }
        };
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
