package cn.paul.netty.future;

import ch.qos.logback.core.util.TimeUtil;
import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TestNettyFuture {
    private static final Logger logger = LoggerFactory.getLogger(TestNettyFuture.class);

    public static void main(String[] args) throws Exception {
        //1. 创建DefaultEventLoopGroup 从而得到DefaultEventEventLoop
        DefaultEventLoopGroup executors = new DefaultEventLoopGroup();
        EventLoop loop = executors.next();
        //2. 得到异步线程后提交相关的任务 并且返回对应的future
        Future<Integer> future = loop.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                logger.debug("异步线程执行任务中》》》》");
                TimeUnit.SECONDS.sleep(7);
                return 999;
            }
        });
        //2.1 使用同步阻塞的方式来获取结果
//        logger.debug("可以接受异步处理了》》》》");
//        logger.debug("异步处理的结果》》》{}",future.get());
        logger.debug("-------------------------------------");

        //2.2 使用异步监听的方式来将异步线程执行连贯的动作
        future.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                logger.debug("可以接受异步处理了》》》》");
                logger.debug("异步处理的结果》》》{}",future.get());
            }
        });
    }
}
