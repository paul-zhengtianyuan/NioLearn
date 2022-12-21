package cn.paul;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * 用于测试NIO操作文件的API
 */
public class NIOFileTest {


    /**
     * 测试写入文件内容
     */
    @Test
    public void test01() throws Exception{
        //1.0 通过FileOutputStream获取channel管道
        FileChannel channel = new FileOutputStream("data1").getChannel();
        //2.0 写入buffer数据
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode("hello paul");
        //3.0 写入文件
        channel.write(byteBuffer);
    }


    /**
     * 经典的IO操作进行文件复制
     */
    @Test
    public void test02()  throws Exception{
        FileInputStream fileInputStream = new FileInputStream("E:\\Project\\NioLearn\\src\\data.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\Project\\NioLearn\\src\\data02.txt");

        while (true){
            byte[] bytes = new byte[1024];
            //将输入流的数据传递给byte数组，返回值为数组的最后一个有值的下标
            int read = fileInputStream.read(bytes);
            if (-1 == read) {
                break;
            }

            //将数组的内容写到输出流
            fileOutputStream.write(bytes,0,read);

        }
    }

    /**
     * 使用CommonIo 提供的API来实现
     */
    @Test
    public void test03() throws Exception{
        FileInputStream fileInputStream = new FileInputStream("E:\\Project\\NioLearn\\src\\data.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\Project\\NioLearn\\src\\data02.txt");

        //调用CommonIO 的 API来实现复制
        IOUtils.copy(fileInputStream,fileOutputStream);

    }


    /**
     * 使用NIO的Channel操作
     */
    @Test
    public void test04() throws Exception{
        FileChannel from = new FileInputStream("E:\\Project\\NioLearn\\src\\data.txt").getChannel();
        FileChannel to = new FileOutputStream("E:\\Project\\NioLearn\\src\\data02.txt").getChannel();

        from.transferTo(0,from.size(),to);
    }


    /**
     * 使用NIO的Channel操作 --- 大于2GB的情况
     */
    @Test
    public void test05() throws Exception{
        FileChannel from = new FileInputStream("E:\\Project\\NioLearn\\src\\data.txt").getChannel();
        FileChannel to = new FileOutputStream("E:\\Project\\NioLearn\\src\\data02.txt").getChannel();


        long left = from.size(); //定量

        while (left >  0){
            long actuallyTrans = from.transferTo(from.size() - left , left, to);
            left = left - actuallyTrans; //此时的left为剩余还没读的
        }
    }
}
