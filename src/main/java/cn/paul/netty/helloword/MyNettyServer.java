package cn.paul.netty.helloword;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 小结: 创建服务端 -- 连接 -- 读写
 */
public class MyNettyServer {
    private static final Logger logger = LoggerFactory.getLogger(MyNettyServer.class);

    public static void main(String[] args) {
        //1. 创建一个服务端类
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        //2. 注册ServerSocketChannel
        serverBootstrap.channel(NioServerSocketChannel.class);
        //3. 引入事件循环监听组 用于监听accept、read、write状态
        serverBootstrap.group(new NioEventLoopGroup());
        //4. 让socketChannel起作用
        serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                //4.1 增加 读取字符 处理器
                //备注: 如果没有StringDecoder,那么在下面拿到的msg就是Netty封装的ByteBuf(对应NIO的ByteBuffer)
                ch.pipeline().addLast(new StringDecoder());
                //4.2 增加 获取数据 处理器
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        String msgRead = (String) msg;
//                        System.out.println("msgRead = " + msgRead);
                        logger.debug("msgRead:{}",msgRead);
                        //后面就是一个业务处理环节....
                    }
                });
            }
        });

        //5. 绑定对应的端口
        serverBootstrap.bind(8000);
    }
}
