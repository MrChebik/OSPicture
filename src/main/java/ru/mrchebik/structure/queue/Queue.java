package ru.mrchebik.structure.queue;

/**
 * Created by mrchebik on 18.03.17.
 */
public class Queue {
    private int rear;
    private int front;
    private long[] queueArray;

    public Queue(int size) {
        rear = -1;
        front = 0;
        queueArray = new long[size + 1];
    }

    public void insert(long item) {
        if (rear == queueArray.length - 1) {
            rear = -1;
        }
        queueArray[++rear] = item;
    }

    public long remove() {
        long temp = queueArray[front++];
        if (front == queueArray.length) {
            front = 0;
        }

        return temp;
    }

    public boolean isEmpty() {
        return rear + 1 == front || (front + queueArray.length - 1 == rear);
    }
}
