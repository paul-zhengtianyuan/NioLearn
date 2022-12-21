package cn.paul.netty.helloword;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;

/**
 * Netty客户端
 */
public class MyNettyClient {
    public static void main(String[] args) throws InterruptedException {
        //1. 创建客户端并且简历ServerSocketChannel管道
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);

        //2.绑定监听组
        bootstrap.group(new NioEventLoopGroup());

        //3.添加处理器
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>(){

            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                //3.1 增加字符encoder
                nioSocketChannel.pipeline().addLast(new StringEncoder());
            }
        });

        //4. 连接服务端
        //4.1 注意这里连接服务端是异步操作的,所以我们必须等到连接成功后进行后面的操作
        //4.2 这里为何要做成异步呢？这是因为异步可以将连接的动作和IO的动作进行分解
        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
        //a. 这里可以理解为一个单独的线程来完成连接操作
        connect.sync();
        //b. 这里可以理解为是另外一个线程来进行IO写操作
        Channel channel = connect.channel();
//        channel.writeAndFlush("hello Netty");
        System.out.println("------------------");
    }
}
