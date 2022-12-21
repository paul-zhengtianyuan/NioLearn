package cn.paul.netty.future;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TestNettyPromise {
    private static final Logger logger = LoggerFactory.getLogger(TestNettyPromise.class);

    public static void main(String[] args) throws Exception{
        //1. 建立eventLoop
        EventLoop eventLoop = new DefaultEventLoop();
        EventLoop next = eventLoop.next();
        //2. 获取promise
        Promise<Integer> promise = next.newPromise();
//        DefaultPromise<Object> defaultPromise = new DefaultPromise<>(next);

        //3. 创建一个线程来异步执行
        new Thread(()->{
            logger.debug("异步处理中.......");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                if (1==2){
                    throw new RuntimeException();
                }
            }catch (Exception e){
                promise.setFailure(e);
            }
            //3.1 设置返回结果
            promise.setSuccess(666);

        }).start();

        //4. 获取返回的结果
        //4.1 阻塞获取返回的结果
//        logger.debug("可以获取异步执行返回的结果了》》》》");
//        logger.debug("返回的结果为:{}",promise.get());
        //4.2 通过增加监听器来在同一个异步线程中执行
        //下面这一步是在异步线程中执行的
        promise.addListener(new GenericFutureListener<Future<? super Integer>>() {
            @Override
            public void operationComplete(Future<? super Integer> future) throws Exception {
                logger.debug("可以获取异步执行返回的结果了》》》》");
                try {
                    logger.debug("返回的结果为:{}",future.get());
                }catch (Exception e){
                    logger.debug("异常信息》》》{}",e.getLocalizedMessage());
                }
            }
        });


        logger.debug("》》》》》》》》》》》》》》》》》》");


    }
}
