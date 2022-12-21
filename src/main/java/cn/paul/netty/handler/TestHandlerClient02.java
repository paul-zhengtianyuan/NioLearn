package cn.paul.netty.handler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class TestHandlerClient02 {
    public static void main(String[] args) throws Exception {
        //1.0 创建对应的客户端
        Bootstrap bootstrap = new Bootstrap();
        //2.0 创建
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(nioEventLoopGroup);

        //3.0 添加处理器
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //a. 添加日志处理器
                ch.pipeline().addLast(new LoggingHandler());
                //b. 添加字符解码器
                ch.pipeline().addLast(new StringEncoder());
            }
        });
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
        //等待连接
        connect.sync();
        //客户端往服务端写出数据
        Channel channel = connect.channel();

        ChannelFuture channelFuture = channel.writeAndFlush("Hello Handler...");
        channelFuture.sync();
        //关闭当前channel
        channel.close();
        //关闭整个客户端
        nioEventLoopGroup.shutdownGracefully();


    }
}
