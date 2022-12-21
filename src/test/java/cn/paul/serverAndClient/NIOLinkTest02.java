package cn.paul.serverAndClient;


import com.sun.org.apache.bcel.internal.generic.Select;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.AbstractSelectionKey;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO 测试 服务端的写操作
 */
public class NIOLinkTest02 {


    /**
     * 客户端代码
     */
    @Test
    public void myClient01() throws Exception{
        //1.0 声明对应的SocketChannel
        SocketChannel socketChannel = SocketChannel.open();
        //2.0 连接对应的端口
        socketChannel.connect(new InetSocketAddress(8000));
        //3.0 从channel中读取数据
        int read = 0;
        while (true){
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 1024);
            read += socketChannel.read(byteBuffer);
            System.out.println("read = " + read);
            byteBuffer.clear();
        }

    }


    /**
     * 服务端代码
     */
    @Test
    public void myServer01() throws Exception{
        //1.0 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8000));
        serverSocketChannel.configureBlocking(false);
        //1.1 绑定对应的状态
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT,null);
        while (true){
            //2.0 选择器进行状态监听
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //3.0 遍历selectionKeys
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();
                
                if (selectionKey.isAcceptable()){
                    //为客户端的连接状态的channel
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    System.out.println("服务器已经连接到客户端..."+ socketChannel);

                    socketChannel.configureBlocking(false);
                    //为socketChannel注册状态
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    //往客户端写数据
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 200000000; i++) {
                        sb.append("a");
                    }

                    ByteBuffer writeBuffer = Charset.defaultCharset().encode(sb.toString());
                    //key1: 因为socketChannel 不可能一下子把全部数据都写出去，是分包写出去的 所以需要循环来写出
                    while (writeBuffer.hasRemaining()){
                        int write = socketChannel.write(writeBuffer);
                        System.out.println("write = " + write);
                    }
                }
            }
        }
    }



    /**
     * 服务端代码: 解决发送端发送空包问题
     */
    @Test
    public void myServer02() throws Exception{
        //1.0 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8000));
        serverSocketChannel.configureBlocking(false);
        //1.1 绑定对应的状态
        Selector selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        while (true){
            //2.0 选择器进行状态监听
            selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            //3.0 遍历selectionKeys
            Iterator<SelectionKey> selectionKeyIterator = selectionKeys.iterator();
            while (selectionKeyIterator.hasNext()) {
                SelectionKey selectionKey = selectionKeyIterator.next();
                selectionKeyIterator.remove();

                if (selectionKey.isAcceptable()){
                    //为客户端的连接状态的channel
                    ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
                    SocketChannel socketChannel = channel.accept();
                    System.out.println("服务器已经连接到客户端..."+ socketChannel);

                    socketChannel.configureBlocking(false);
                    //为socketChannel注册状态
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    //往客户端写数据
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 20000000; i++) {
                        sb.append("a");
                    }

                    ByteBuffer writeBuffer = Charset.defaultCharset().encode(sb.toString());
                    //先写第一次
                    int write = socketChannel.write(writeBuffer);
                    System.out.println("write = " + write);
                    //如果存在没有还没有写完的情况下
                    if (writeBuffer.hasRemaining()){
                        //监听写的状态 并 对未写完的buffer与该channel进行绑定
                        selectionKey.interestOps(selectionKey.interestOps() + SelectionKey.OP_WRITE);//在原来的基础上相加
                        selectionKey.attach(writeBuffer);
                    }
                }else if (selectionKey.isWritable()){
                    //若该状态的channel为可写，说明仍然有数据没有写完，需要接着写
                    //拿到对应的channel
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    //拿到对应的buffer
                    ByteBuffer attachmentBuffer = (ByteBuffer) selectionKey.attachment();
                    int write = socketChannel.write(attachmentBuffer);
                    System.out.println("write = " + write);
                    //如果已经写完了 取消其状态与置空附件
                    if (!attachmentBuffer.hasRemaining()){
                        selectionKey.attach(null);// 这个方法有取代的功能
                        selectionKey.interestOps(selectionKey.interestOps() - SelectionKey.OP_WRITE);
                    }

                }
            }
        }
    }

}
