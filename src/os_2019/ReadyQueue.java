package os_2019;

import java.util.*;
public class ReadyQueue {

    private ArrayList<Process> arr;         // array to store queue elements
    private int front;         // front points to front element in the queue
    private int rear;          // rear points to last element in the queue
    private int capacity;      // maximum capacity of the queue
    private int count;         // current size of the queue

    // Constructor to initialize queue
    ReadyQueue(int size)
    {

        arr = new ArrayList<Process>();
        capacity = size;
        front = 0;
        rear = -1;
        count = 0;
    }

    // Utility function to remove front element from the queue
    public Process dequeue()
    {
        // check for queue underflow
        if (isEmpty())
        {
            System.out.println("UnderFlow\nProgram Terminated");
            System.exit(1);
        }
        Process p = arr.get(0);
        arr.remove(0);
//        Process p = arr.get(front);
////        System.out.println("Removing " + arr.get(front));
        front = (front + 1) % capacity;
        count--;
        return p;
    }

    // Utility function to add an item to the queue
    public void enqueue(Process item)
    {
        // check for queue overflow
        if (isFull())
        {
            System.out.println("OverFlow\nProgram Terminated");
            System.exit(1);
        }

//        System.out.println("Inserting " + item.toString());
        rear = (rear + 1) % capacity;
//        System.out.println(rear);
        arr.add(item);
//        arr.add(rear,item);
        count++;
    }

    // Utility function to return front element in the queue
    public Process peek()
    {
        if (isEmpty())
        {
            System.out.println("UnderFlow\nProgram Terminated");
            System.exit(1);
        }
        return arr.get(0);
//        return arr.get(front);
    }

    // Utility function to return the size of the queue
    public int size()
    {
        return count;
    }

    // Utility function to check if the queue is empty or not
    public Boolean isEmpty()
    {
        return (size() == 0);
    }

    // Utility function to check if the queue is empty or not
    public Boolean isFull()
    {
        return (size() == capacity);
    }

    public void addSRTF (Process p) {
//        if (arr.isEmpty()) {
//            arr.add(p);
//        } else {
            int i = 0;
            Process flag = null;
            for (Process proc : arr) {
                if (proc.remainingTime > p.remainingTime) {
//                    arr.add(i,p);
                    flag = proc;
//                    System.out.println("Added");
                    break;
                }
                i++;
            }
            if (flag != null)
                arr.add(i, p);
            else
                arr.add(p);

            count++;
            rear = (rear + 1) % capacity;


        }
    }
//}

