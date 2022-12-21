package cn.paul.NIO;

import cn.paul.constant.CommonConstants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用RandomAccessFile来实现文件读取
 */
public class NIOTest02 {
    public static void main(String[] args) {

        //1. 通过RandomAccessFile获取Channel对象
        try ( FileChannel fileChannel = new RandomAccessFile(CommonConstants.FILE_URL,"rw").getChannel()){

            //1.1 创建buffer对象 并声明相应的大小
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);

            //2. 将channel的字节流读取到buffer对象里面
            while (true){
                int read = fileChannel.read(byteBuffer);
                if (read == -1){
                    break;
                }
                //3. 开启buffer的读模式，将其读取到程序中
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    System.out.println("b = " + ((char) b));
                }
                //4. 读完后 将buffer调整回写模式 以便下一波阅读
                byteBuffer.clear();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
