package cn.paul.netty.handler;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 该类为测试内置的Handler 以便于测试handler
 */
public class TestEmbedHandler {
    private static final Logger logger = LoggerFactory.getLogger(TestEmbedHandler.class);

    public static void main(String[] args) {
        //1. 创建读入数据的Handler h1 -> h2 -> h3
        ChannelInboundHandlerAdapter h1 = new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.debug("h1 and h1's msg is:{}",msg);
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h2 = new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.debug("h2");
                super.channelRead(ctx, msg);
            }
        };

        ChannelInboundHandlerAdapter h3 = new ChannelInboundHandlerAdapter(){
            @Override
            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                logger.debug("h3");
                super.channelRead(ctx, msg);
            }
        };

        //2. 创建写出数据的handler h6 -> h5 -> h4
        ChannelOutboundHandlerAdapter h4 = new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.debug("h4");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter h5 = new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.debug("h5");
                super.write(ctx, msg, promise);
            }
        };

        ChannelOutboundHandlerAdapter h6 = new ChannelOutboundHandlerAdapter(){
            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                logger.debug("h6");
                super.write(ctx, msg, promise);
            }
        };

        //3. 创建内置的handler处理器来测试
        EmbeddedChannel embeddedChannel = new EmbeddedChannel(h1,h2,h3,h4,h5,h6);

        //3.1 往内置的handler中写入数据
//      embeddedChannel.writeInbound("hello inboundHandler....");

        //3.1 按照Netty的初步封装 实际上拿到手的第参数应该是ByteBuf, 我们在这里进行适当的模拟一下
        embeddedChannel.writeInbound(ByteBufAllocator.DEFAULT.buffer().writeBytes("hello inboundHandler".getBytes(StandardCharsets.UTF_8)));

        //3.2 往内置的handler中写出数据
        embeddedChannel.writeOutbound("hello outboundHandler....");

    }
}
