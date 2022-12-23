package cn.paul;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class NIOTest {
    private static final Logger logger = LoggerFactory.getLogger(NIOTest.class);


    /**
     * 1.0 测试新创建buffer的三大特性
     */
    @Test
    public void test01(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        int capacity = byteBuffer.capacity(); //6
        System.out.println("capacity = " + capacity);
        int position = byteBuffer.position();//0
        System.out.println("position = " + position);
        int limit = byteBuffer.limit();
        System.out.println("limit = " + limit);//6

    }


    /**
     * 2.0 测试新buffer的读写模式的三大特性
     */
    @Test
    public void test02(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(new byte[]{'a','b','c','d'});
        int capacity = byteBuffer.capacity(); //6
        System.out.println("capacity = " + capacity);
        int position = byteBuffer.position();//4
        System.out.println("position = " + position);
        int limit = byteBuffer.limit();
        System.out.println("limit = " + limit);//6

    }

    /**
     * 3.0 测试新buffer的调整为读模式的三大特性
     */
    @Test
    public void test03(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(new byte[]{'a','b','c','d'});
        byteBuffer.flip();
        int capacity = byteBuffer.capacity(); //6
        System.out.println("capacity = " + capacity);
        int position = byteBuffer.position();//0
        System.out.println("position = " + position);
        int limit = byteBuffer.limit();
        System.out.println("limit = " + limit);//4

    }

    /**
     * 4.0 测试buffer调用clear方法回到写模式
     */
    @Test
    public void test04(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(new byte[]{'a','b','c','d'});
        byteBuffer.flip();
        byteBuffer.clear();
        int capacity = byteBuffer.capacity(); //6
        System.out.println("capacity = " + capacity);
        int position = byteBuffer.position();//0
        System.out.println("position = " + position);
        int limit = byteBuffer.limit();
        System.out.println("limit = " + limit);//6

    }

    /**
     * 5.0 测试buffer调用compact方法压缩到
     */
    @Test
    public void test05(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(new byte[]{'a','b','c','d'});
        byteBuffer.flip();
        //读取头两个元素
        byte first = byteBuffer.get();
        System.out.println("first = " + ((char) first));

        byte second = byteBuffer.get();
        System.out.println("second = " + ((char) second));

        //查询读取两个元素后的特性
        int capacity = byteBuffer.capacity(); //6
        System.out.println("capacity = " + capacity);
        int position = byteBuffer.position();//2
        System.out.println("position = " + position);
        int limit = byteBuffer.limit();
        System.out.println("limit = " + limit);//4

        System.out.println("======================================");

        //调用compact方法,作用：1.将未读完的元素前挪 2. 将buffer调整为写模式
        byteBuffer.compact();
        int compactAfter = byteBuffer.capacity();
        System.out.println("compactAfter = " + compactAfter);//6
        int compactAfterPosition = byteBuffer.position();
        System.out.println("compactAfterPosition = " + compactAfterPosition);//2
        int compactAfterLimit = byteBuffer.limit();
        System.out.println("compactAfterLimit = " + compactAfterLimit);//6
        //获取下标为2和3的元素 验证其是否为一种复制模式
        byteBuffer.flip();//虽然不调整其为读模式，下面通过下标的方式获取元素值不会报错，但是这是一种符合逻辑的编程好习惯

        /**
         * 备注: 如果下面获取下标为2或3的元素是会报indexOfBoundsException的
         * 说明其跨越了position指针
         */
        /*byte indexTwo = byteBuffer.get(2);
        System.out.println("indexTwo = " + ((char) indexTwo));
        byte indexThree = byteBuffer.get(3);
        System.out.println("indexThree = " + ((char) indexThree));*/

    }

    /**
     * 6.0 测试rewind方法，用于数据的复用
     */
    @Test
    public void test06(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(new byte[]{'a','b','c','d'});
        byteBuffer.flip();
        //读取两个数据
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        //回到position为0 的值位置重读
        byteBuffer.rewind();
        //读取两个数据
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));

    }

    /**
     * 7.0 测试mark&reset方法
     */
    @Test
    public void test07(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(6);
        byteBuffer.put(new byte[]{'a','b','c','d'});
        byteBuffer.flip();
        //读取两个数据
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        //标记当前position指针的位置
        byteBuffer.mark();
        //读取两个数据
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        byteBuffer.reset();
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
        System.out.println("byteBuffer.get() = " + ((char) byteBuffer.get()));
    }

    /**
     * 8.0 将数据存储在buffer中
     */
    @Test
    public void test08(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put("tiantian".getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();
        while(byteBuffer.hasRemaining()){
            System.out.println(((char) byteBuffer.get()));
        }
        byteBuffer.clear();
    }


    /**
     * 9.0 将数据存储在buffer中
     */
    @Test
    public void test09(){
        //通过encode方法获取的buffer就已经是读模式了，不要再调用flip方法
        ByteBuffer byteBuffer = Charset.forName("UTF-8").encode("tiantian");
        while(byteBuffer.hasRemaining()){
            System.out.println(((char) byteBuffer.get()));
        }
        byteBuffer.clear();
    }

    /**
     * 10. 将数据转为字符串 理解
     *  测试代码
     */
    @Test
    public void test10(){
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put("天".getBytes());
        //注: 必须将其转为读模式才有效
        byteBuffer.flip();
        CharBuffer charBuffer = Charset.forName("UTF-8").decode(byteBuffer);
        System.out.println(charBuffer.toString());

    }

}
