package cn.paul.netty.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class TestHandlerServer01 {
    private static final Logger logger = LoggerFactory.getLogger(TestHandlerServer01.class);

    public static void main(String[] args) throws Exception{
        //1.0 创建服务端
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.channel(NioServerSocketChannel.class);
        //2.0 引入线程组
        NioEventLoopGroup bossEventLoop = new NioEventLoopGroup();
        serverBootstrap.group(bossEventLoop);

        //3.0 添加流水线处理器handler
        serverBootstrap.childHandler(new ChannelInitializer(){

            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("handler01",new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        logger.debug("handler01 is working");
                        //将msg进行转换，因为其实Netty将数据存储在ByteBuf中
                        ByteBuf getResult = (ByteBuf) msg;
                        String decode = getResult.toString(Charset.forName("UTF-8"));
                        logger.debug("解密后的结果decode为:{}",decode);
                        super.channelRead(ctx, decode);// 将结果继续透传递下去,注意这里透传的是解压后的字符串结果
                    }
                });

                pipeline.addLast("handler02",new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        logger.debug("handler02 is working and get the result from the handler01 is:{}",msg);
                        //无需透传结果，所以不需调用透传代码
                        //备注: 如果不调用这个API 下一个handler 也不会被调用到
                    }
                });

                pipeline.addLast("handler03",new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        logger.debug("handler03 is working and get the result from the handler02 is:{}",msg);

                    }
                });
            }
        });

        //4.0 绑定对应的端口
        serverBootstrap.bind(8000);

    }
}
