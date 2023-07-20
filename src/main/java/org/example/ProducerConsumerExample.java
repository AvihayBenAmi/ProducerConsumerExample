package org.example;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerExample {
    private static final int CAPACITY = 5;
    private static final Queue<Integer> buffer = new LinkedList<>();
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Thread producerThread = new Thread(() -> {
            try {
                produce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread consumerThread = new Thread(() -> {
            try {
                consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        producerThread.start();
        consumerThread.start();
    }

    public static void produce() throws InterruptedException {
        int value = 0;
        while (true) {
            synchronized (lock) {
                while (buffer.size() == CAPACITY) {
                    System.out.println("Producer is waiting as buffer is full");
                    lock.wait();
                }
//                System.out.println("Producer produces: " + value);
                buffer.add(value++);
                System.out.println("Items in buffer: " + buffer.size());
                lock.notifyAll();
            }
            Thread.sleep(1000);
        }
    }

    public static void consume() throws InterruptedException {
        while (true) {
            synchronized (lock) {
                while (buffer.isEmpty()) {
                    System.out.println("Consumer is waiting as buffer is empty");
                    lock.wait();
                }
                int itemsToConsume = (int) (Math.random() * buffer.size()) + 1;
                System.out.println("Consumer wants to consume: " + itemsToConsume);
                for (int i = 0; i < itemsToConsume; i++) {
                    int value = buffer.poll();
//                    System.out.println("Consumer consumes: " + value);
                }
                System.out.println("Items in buffer: " + buffer.size());
                lock.notifyAll();
            }
            Thread.sleep(3000);
        }
    }
}
