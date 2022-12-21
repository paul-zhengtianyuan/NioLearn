package cn.paul.netty.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class TestHandlerServer03 {
    private static final Logger logger = LoggerFactory.getLogger(TestHandlerServer03.class);

    public static void main(String[] args) throws Exception{
        //1.0 创建服务端
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        //2.0 引入线程组
        NioEventLoopGroup bossEventLoop = new NioEventLoopGroup();
        serverBootstrap.group(bossEventLoop);

        //为 nioServerSocketChannel 建立连接
        serverBootstrap.handler(new ChannelInitializer<NioServerSocketChannel>() {

            @Override
            protected void initChannel(NioServerSocketChannel ch) throws Exception {

            }
        });

        //3.0 添加流水线处理器handler
        /*serverBootstrap.childHandler(new ChannelInitializer(){

            @Override
            protected void initChannel(Channel ch) throws Exception {

                ChannelPipeline pipeline = ch.pipeline();

                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        logger.debug("result is {}",msg);
                        super.channelRead(ctx, msg);
                    }
                });

            }
        });*/

        //4.0 绑定对应的端口
        serverBootstrap.bind(8000);

    }
}
