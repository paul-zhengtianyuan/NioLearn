package cn.paul.netty.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import org.junit.jupiter.api.Test;

public class ByteBufDemo {
    public static void main(String[] args) {
        //1.0 创建一个新的ByteBuf
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(10);
        byteBuf.writeByte('a');//输入一个字符(占用一个字节)
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        byteBuf.writeInt(3);

        //2.0 观察
        System.out.println(byteBuf.toString());//观察现在ByteBuf的容量和指针情况
        System.out.println(ByteBufUtil.prettyHexDump(byteBuf));//以十六进制的格式打印内容出来


    }


    /**
     * 测试ByteBuf的API来创建堆内存和直接内存
     */
    @Test
    public void testDirectOrHeapByteBuf(){
        //1. 使用默认的API创建的是直接内存
        ByteBuf buffer01 = ByteBufAllocator.DEFAULT.buffer(10);
        System.out.println("buffer01 = " + buffer01);

        //2. 显式创建直接内存
        ByteBuf directByteBuf = ByteBufAllocator.DEFAULT.directBuffer();
        System.out.println("directByteBuf = " + directByteBuf);

        //3. 显式创建堆内存
        ByteBuf heapByteBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        System.out.println("heapByteBuf = " + heapByteBuf);
    }

}
