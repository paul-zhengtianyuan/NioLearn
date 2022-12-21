package cn.paul.serverAndClient;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectionKey;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class NIOLinkTest01 {


    /**
     * 模拟NIO的客户端
     */
    @Test
    public void test01() throws Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress(8000));
        System.out.println("---------客户端连接---------");
    }

    /**
     * 第一版: 存在阻塞问题
     * @throws Exception
     */
    @Test
    public void test02() throws Exception{
        //1.0 新创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.0 绑定要监听的端口
        serverSocketChannel.bind(new InetSocketAddress(8081));

        //3.0 准备buffer, 以便于将channel流动的数据读取到buffer里面
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);

        //3.1 每次连接都会创建一个管道(这个管道好像是可以复用的),但是每次连接所生成的channel对象确实不一样的
        List<SocketChannel> channels = new ArrayList<>();

        //4.0 因为服务端一直开启，使用死循环模拟
        while (true){
            System.out.println("等待连接客户端...");
            SocketChannel clientChannel = serverSocketChannel.accept();//面临等待客户端连接的阻塞
            System.out.println("服务器已经连接到客户端..."+ clientChannel);

            channels.add(clientChannel);

            for (SocketChannel channel : channels) {
                //4.1 从channel 管道中读取字节到buffer里面
                System.out.println("开始实际的数据通信....");
                channel.read(byteBuffer);//面临等待数据的阻塞

                byteBuffer.flip();
                //4.2 输出传输的内容
                CharBuffer decodeChar = Charset.forName("UTF-8").decode(byteBuffer);
                System.out.println("decodeChar.toString() = " + decodeChar.toString());
                //4.3 读取完毕 设置回来写模式
                byteBuffer.clear();
                System.out.println("通信已经结束....");
            }
        }

    }


    /**
     * 第二版: 解决ServerSocketChannel 的阻塞问题
     */
    @Test
    public void test03() throws Exception{
        //1.0 新创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.0 绑定要监听的端口
        serverSocketChannel.bind(new InetSocketAddress(8081));
        //2.1 使得serverSocketChannel处于非阻塞状态
        serverSocketChannel.configureBlocking(false);

        //3.0 准备buffer, 以便于将channel流动的数据读取到buffer里面
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);

        //3.1 每次连接都会创建一个管道(这个管道好像是可以复用的),但是每次连接所生成的channel对象确实不一样的
        List<SocketChannel> channels = new ArrayList<>();
        try {
            //4.0 因为服务端一直开启，使用死循环模拟
            while (true){
                SocketChannel clientChannel = serverSocketChannel.accept();//如果accept不到会返回一个null

                if (null != clientChannel) {
                    channels.add(clientChannel);
                    System.out.println("服务器已经连接到客户端..."+ clientChannel);
                }
                System.out.println("channels.size() = " + channels.size());
                for (SocketChannel channel : channels) {
                    //4.1 从channel 管道中读取字节到buffer里面
                    System.out.println("开始实际的数据通信....");
                    channel.read(byteBuffer);//面临等待数据的阻塞

                    byteBuffer.flip();
                    //4.2 输出传输的内容
                    CharBuffer decodeChar = Charset.forName("UTF-8").decode(byteBuffer);
                    System.out.println("decodeChar.toString() = " + decodeChar.toString());
                    //4.3 读取完毕 设置回来写模式
                    byteBuffer.clear();
                    System.out.println("通信已经结束....");
                }
            }
        }catch (Exception e){
            System.out.println("客户端关闭.....服务端程序退出...");
        }
    }

    /**
     * 第三版: 解决SocketChannel 的阻塞问题
     */
    @Test
    public void test04() throws Exception{
        //1.0 新创建一个ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.0 绑定要监听的端口
        serverSocketChannel.bind(new InetSocketAddress(8081));
        //2.1 使得serverSocketChannel处于非阻塞状态
        serverSocketChannel.configureBlocking(false);

        //3.0 准备buffer, 以便于将channel流动的数据读取到buffer里面
        ByteBuffer byteBuffer = ByteBuffer.allocate(20);

        //3.1 每次连接都会创建一个管道(这个管道好像是可以复用的),但是每次连接所生成的channel对象确实不一样的
        List<SocketChannel> channels = new ArrayList<>();
        try {
            //4.0 因为服务端一直开启，使用死循环模拟
            while (true){
                SocketChannel clientChannel = serverSocketChannel.accept();//如果accept不到会返回一个null

                if (null != clientChannel) {
                    channels.add(clientChannel);
                    //4.1 开启socketChannel的非阻塞模式
                    clientChannel.configureBlocking(false);
                    System.out.println("服务器已经连接到客户端..."+ clientChannel);
                }
                for (SocketChannel channel : channels) {
                    //4.2 从channel 管道中读取字节到buffer里面
                    int read = channel.read(byteBuffer);//如果没有数据可读会返回0
                    if (read > 0){
                        System.out.println("channels.size() = " + channels.size());
//                        System.out.println("read:"+read);
                        System.out.println("开始实际的数据通信....");
                        byteBuffer.flip();
                        //4.3 输出传输的内容
                        CharBuffer decodeChar = Charset.forName("UTF-8").decode(byteBuffer);
                        System.out.println("decodeChar.toString() = " + decodeChar.toString());
                        //4.4 读取完毕 设置回来写模式
                        byteBuffer.clear();
                        System.out.println("通信已经结束....");
                    }
                }
            }
        }catch (Exception e){
            System.out.println("客户端关闭.....服务端程序退出...");
        }
    }

    /**
     * 第四版: 引入Selector管理者来实现相关的非阻塞
     */
    @Test
    public void test05() throws Exception{
        //1.0 开启ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2.0 绑定监听的端口与开启非阻塞配置
        serverSocketChannel.bind(new InetSocketAddress(8081));
        serverSocketChannel.configureBlocking(false);
        //3.0 获取管理者对象
        Selector selector = Selector.open();

        //3.1 注册当前的ServerSocketChannel和对应的监听状态到Selector
        SelectionKey selectionKey = serverSocketChannel.register(selector, 0, null);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);//只有配置了让selector监控哪种状态的channel,达到这种状态后才会有所反应
        while (true){
            selector.select();//会一直在等待某个连接或读取数据的出现
            System.out.println("===========================");
            //如果有对应的状态的channel到来 将会继续走下面来
            //3.2 获取具备对应状态的channel对象
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            if (selectionKeyIterator.hasNext()) {
                SelectionKey channelWithState = selectionKeyIterator.next();
                selectionKeyIterator.remove();//只是把集合中的channel删除,上面其实拿到了selectedKeys中channel对象的引用了

                if (channelWithState.isAcceptable()) {
                    //如果为可接受的，那么意味着它是监听到了对应的连接
                    //专门针对ServerSocketChannel进行处理(一般针对一个服务端只有一个)
                    ServerSocketChannel singleServerSocketChannel = (ServerSocketChannel) channelWithState.channel();
                    SocketChannel accept = singleServerSocketChannel.accept();
                    System.out.println("服务器已经连接到客户端..."+ accept);

                    //最重要的: 设置该socketChannel为非阻塞状态
                    //设置非阻塞的代码必须在注册之前
                    accept.configureBlocking(false);
                    //创建一个新的缓冲区 作为该注册channel的附件
                    ByteBuffer byteBuffer = ByteBuffer.allocate(6);
                    //当取得连接后 后面要做的就是监听socketChannel的读写操作了
                    SelectionKey scKey = accept.register(selector, 0, byteBuffer);
                    scKey.interestOps(SelectionKey.OP_READ);

                }else if (channelWithState.isReadable()){
                    try {
                        //针对SocketChannel 因为它是可读的 那么就意味着可以读取里面的数据到buffer里面
                        System.out.println("----isReadable----");
//                        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
                        //获取对应的附件
                        ByteBuffer byteBuffer = (ByteBuffer) channelWithState.attachment();
                        SocketChannel socketChannel = (SocketChannel) channelWithState.channel();
                        socketChannel.configureBlocking(false);
                        int read = socketChannel.read(byteBuffer);
                        if (read == -1) {
                            channelWithState.cancel();
                        }
                        doInSplit(byteBuffer);
                        if (byteBuffer.position() == byteBuffer.limit()){
                            //创建一个新的buffer
                            ByteBuffer newByteBuffer = ByteBuffer.allocate(byteBuffer.capacity() * 2);
                            //调整旧buffer为读模式
                            byteBuffer.flip();
                            //将旧数据移动到新的buffer中
                            newByteBuffer.put(byteBuffer);
                            //替换和channel绑定的附件
                            channelWithState.attach(newByteBuffer);
                        }
                    /*byteBuffer.flip();
                    CharBuffer decode = Charset.defaultCharset().decode(byteBuffer);
                    System.out.println("decode.toString() = " + decode.toString());*/
                    }catch (Exception e){
                        e.printStackTrace();
                        channelWithState.cancel();
                    }
                }else if (channelWithState.isWritable()){
                    System.out.println("writable");
                }

            }
        }

    }

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
