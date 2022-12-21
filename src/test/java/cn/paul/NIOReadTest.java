package cn.paul;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NIOReadTest {

    /**
     * 测试NIO读取资源
     */
    @Test
    public void test01(){
        //1.0 声明一个byteBuffer
        ByteBuffer byteBuffer = ByteBuffer.allocate(50);
        //2.0 往byteBuffer中放入对应的数据
        byteBuffer.put("Hi paul\ni".getBytes(StandardCharsets.UTF_8));
        //3.0 开启字节的切割输出对应的语句
        doInSplit(byteBuffer);
    }

    /**
     * 根据 \n 标识符切割字节
     * @param byteBuffer
     */
    private void doInSplit(ByteBuffer byteBuffer) {
        //1.0 通过limit的指针遍历byteBuffer
        //开启读模式
        byteBuffer.flip();
        for (int i = 0; i < byteBuffer.limit(); i++) {
            if ('\n' == byteBuffer.get(i)) {
                //1.1 若找到了对应的标志位 新建一个byteBuffer用于保存
                int length = i + 1;
                ByteBuffer targetBuffer = ByteBuffer.allocate(length);
                //1.2 通过length的位置去到原来的byteBuffer中取值
                for (int j = 0; j < length; j++) {
                    targetBuffer.put(byteBuffer.get());
                }
                //1.3 设置target的读模式,将字符串输出
                targetBuffer.flip();
                CharBuffer charBuffer = Charset.forName("UTF-8").decode(targetBuffer);
                System.out.println(charBuffer.toString());
                targetBuffer.clear();
            }
        }
        //2.0 整个遍历完成后 调用compact方法将字符前置
        byteBuffer.compact();
    }
}
