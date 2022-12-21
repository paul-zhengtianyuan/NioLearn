package cn.paul.netty.helloword02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * Netty客户端
 */
public class MyNettyClient02plus {
    private static final Logger logger = LoggerFactory.getLogger(MyNettyClient02plus.class);

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

        connect.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                logger.debug("--- add listener ---");
                Channel channel = connect.channel();
                channel.writeAndFlush("hello Netty");
            }
        });

    }
}
