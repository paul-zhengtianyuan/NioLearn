package cn.paul.netty.ByteBuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class TestByteBufApi {
    @Test
    public void testAPI01(){
        //1.0 创建一个默认大小为10的ByteBuf
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);

        //2.0 往buffer中写入一个字节的数据并进行读
        buffer.writeByte(1);
//        buffer.writeByte(2);
        System.out.println("buffer = " + buffer);
        System.out.println(ByteBufUtil.prettyHexDump(buffer));

        //2.1 在buffer中读出一个字节的数据
        byte read01 = buffer.readByte();
        System.out.println("read01 = " + read01);
        //2.2 再次查看buffer的指针变化
        System.out.println("buffer = " + buffer);
        System.out.println(ByteBufUtil.prettyHexDump(buffer));

        //2.3 再次读一次查看具体情况
        byte readAgain = buffer.readByte();
        System.out.println("readAgain = " + readAgain);


    }


    /**
     * 测试ByteBuf重复读
     */
    @Test
    public void testAPI02(){
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);
        //1.0 写入数据
        buffer.writeByte(1);
        buffer.writeByte(1);
        buffer.writeByte(0);

        //2.0 标记现在的读指针
        buffer.markReaderIndex();


        //3.0 读部分数据出来
        byte read01 = buffer.readByte();
        System.out.println("read01 = " + read01);
        System.out.println(buffer);

        //3.1 回到对应读标记的位置 再次读
        buffer.resetReaderIndex();
        System.out.println(buffer);
    }

}
