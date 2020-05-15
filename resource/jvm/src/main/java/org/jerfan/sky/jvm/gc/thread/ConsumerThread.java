package org.jerfan.sky.jvm.gc.thread;

public class ConsumerThread extends Thread implements Runnable{

    private long size;


    public ConsumerThread(long  size){
        this.size = size;
    }
    @Override
    public void run() {
        while (size >0){
            System.out.println("consumer ing");
            System.out.println("thread name is :"+Thread.currentThread().getName());
            System.out.println("thread status is :"+Thread.currentThread().getState().name());
            try{
                Data data =Box.QUEUE.remove();
                System.out.println("remove :"+data);
                size--;
            }catch (Exception e){
                System.out.println("queue is empty ");

            }

        }

    }
}
