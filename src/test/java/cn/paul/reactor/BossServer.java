package cn.paul.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 主从架构的Reactor模式 之 Boss线程
 */
public class BossServer {
    private static final Logger logger = LoggerFactory.getLogger(BossServer.class);

    //外部定义计数器
    private static AtomicInteger count = new AtomicInteger(0);

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8000));
        serverSocketChannel.configureBlocking(false);
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT, null);

        //先在外层定义一个Worker类
        Worker[] workers = new Worker[2];
        for (int i = 0; i < workers.length; i++) {
            workers[i] = new Worker("worker_"+i);
        }

        while (true){
            selector.select();
            //从selector中获取具备对应状态的SocketChannel
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                //将对应的状态清除
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    //如果是连接的channel到来
                    ServerSocketChannel ssc = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = ssc.accept();
                    socketChannel.configureBlocking(false);
                    //因为要从ssc中读取数据 所以要作为参数透传下去
                    logger.debug("boos register the worker");
                    //通过最简单的hash取模的方式来获取对应的Worker
                    Worker worker = workers[count.getAndIncrement() % workers.length];
                    worker.register(socketChannel);
                    logger.debug("boos registered the worker");
                }

            }

        }
    }
}
