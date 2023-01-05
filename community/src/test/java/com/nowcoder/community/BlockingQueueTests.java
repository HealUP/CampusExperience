package com.nowcoder.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
* Description: 测试阻塞队列
* date: 2023/1/5 16:38
 *
* @author: Deng
* @since JDK 1.8
*/
public class BlockingQueueTests {

    public static void main(String[] args) {
        BlockingQueue queue = new ArrayBlockingQueue(10);//底层实现是数组 初始化为10
        //一个生产线程
        new Thread(new Producer(queue)).start();
        //3个消费线程
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }

}

class Producer implements Runnable {
//初始化队列
    private BlockingQueue<Integer> queue;

    public Producer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 100; i++) {
                Thread.sleep(20);
                queue.put(i);
                System.out.println(Thread.currentThread().getName() + "生产:" + queue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

class Consumer implements Runnable {

    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(new Random().nextInt(1000));//随机休息时间 0-1000中取
                queue.take();//取出值
                System.out.println(Thread.currentThread().getName() + "消费:" + queue.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}