package cn.paul.NIO;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 基础Netty编程
 * 测试git
 */
public class NIOTest01 {
    public static void main(String[] args) throws Exception {

        //1. 通过文件流获取Channel管道

        FileChannel channel = new FileInputStream("src/data.txt").getChannel();

        //2. 通过ByteBuffer创建对象并分配对应的字节空间
        //备注1: 对于UTF-8体系下 数字占一个字节 中文占2个字节
        //备注2: 分配的空间是固定的
        //备注3: 当新建一个Buffer对象的时候 该管道已经是写模式了
        ByteBuffer buffer = ByteBuffer.allocate(10);

        while(true){


            //3. 通过channel往buffer里面写数据
            //备注: 当返回-1时 说明文件已经读取完毕
            int read = channel.read(buffer);

            if (read == -1) break;

            //4. 切换buffer 为读模式
            buffer.flip();

            //5. 循环获取buffer的数据
            //备注: 读取是一个个字节读取出来的
            while(buffer.hasRemaining()){
                byte b = buffer.get();
                System.out.println("b = " + (char)b);
            }

            //5. 切换buffer回来写模式(可以暂时理解为腾挪buffer的位置)
            buffer.clear();

        }
    }
}
