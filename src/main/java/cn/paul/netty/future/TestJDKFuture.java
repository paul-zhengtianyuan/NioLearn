package cn.paul.netty.future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class TestJDKFuture  {
    private static final Logger logger = LoggerFactory.getLogger(TestJDKFuture.class);

    public static void main(String[] args) throws Exception {
        //1. 创建一个固定线程的线程池
        ExecutorService fixExecutor = Executors.newFixedThreadPool(2);
        //2. 提交对应的任务
        Future<Integer> asyncTask = fixExecutor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                logger.debug("正在异步线程中搬砖......");
                //2.1 睡上几秒
                TimeUnit.SECONDS.sleep(5);
                return 666;
            }
        });

        //3. 主线程阻塞以等待异步线程执行方法的结果
        logger.debug("可以拿到结果啦》》》》》》》");
        Integer result = asyncTask.get();
        logger.debug("result:{}",result);
        logger.debug("拿到结果了,结束吧》》》》》");

    }
}
