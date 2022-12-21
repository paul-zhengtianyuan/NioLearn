package cn.paul.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Worker implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Worker.class);
    //多路复用器
    private Selector selector;
    //线程
    private Thread thread;
    //该线程的名字
    private String name;
    //状态标识
    private volatile boolean isCreated;

    //线程安全队列
    private ConcurrentLinkedQueue<Runnable> runnables = new ConcurrentLinkedQueue<>();

    public Worker(String name){
        this.name = name;
    }

    public void register(SocketChannel sc) throws IOException, InterruptedException {
        //1.0 开启对应的Selector
        if (!isCreated){
            logger.debug("Firstly open the selector");
            this.selector = Selector.open();
            //2.0 启动线程
            this.thread = new Thread(this,name);
            thread.start();
            //1.1 当已经实现了selector的单例化后 就修改其对应的状态
            isCreated = true;
        }
        //3.0 注册监听当前channel的状态
        Thread.sleep(2000);
        logger.debug("sleeping 2s...");
        //放入线程安全队列
        runnables.add(()->{
            try {
                sc.register(selector,SelectionKey.OP_READ);
            } catch (ClosedChannelException e) {
                e.printStackTrace();
            }
        });

        selector.wakeup();

    }

    @Override
    public void run() {
        //1.0 多路复用器
        while (true){
            try {
                logger.debug("worker run method invoke.....");
                selector.select();
                //注册对应的状态
                Runnable poll = runnables.poll();
                if (poll != null) {
                    poll.run();
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey next = iterator.next();
                    iterator.remove();

                    if (next.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) next.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(30);
                        socketChannel.read(byteBuffer);
                        byteBuffer.flip();
                        String result = Charset.defaultCharset().decode(byteBuffer).toString();
                        System.out.println("result = " + result);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
