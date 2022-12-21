package cn.paul.netty;

import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;

public class EventLoopGroupTest {
    public static void main(String[] args) {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
        EventLoop ev1 = eventLoopGroup.next();
        EventLoop ev2 = eventLoopGroup.next();
        EventLoop ev3 = eventLoopGroup.next();
        System.out.println("ev1 = " + ev1);
        System.out.println("ev2 = " + ev2);
        System.out.println("ev3 = " + ev3);
        //通过该方法可以获取计算机的核数(逻辑核数)
        System.out.println(NettyRuntime.availableProcessors() * 2);
    }
}
