package cn.paul.NIO;

import cn.paul.constant.CommonConstants;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.WRITE;

/**
 * 使用FileChannel工具类来获取Channel对象
 */
@SuppressWarnings(value = "All")
public class NIOTest03 {
    public static void main(String[] args) {
        //1. 获取Channel对象
        try (FileChannel fileChannel = FileChannel.open(Paths.get(CommonConstants.FILE_URL), StandardOpenOption.READ)){
            //2. 创建byteBuffer对象
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            //3. 将Channel的字节读取到buffer对象里面
            while (true) {
                int read = fileChannel.read(byteBuffer);
                if (read == -1) break;
                //4. 开启buffer的读模式 将其读取到程序中
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byte b = byteBuffer.get();
                    System.out.println("b = " + ((char) b));
                }
                //5. 将buffer调整回写模式
                byteBuffer.clear();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
