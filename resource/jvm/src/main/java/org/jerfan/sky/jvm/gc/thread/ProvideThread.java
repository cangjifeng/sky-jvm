package org.jerfan.sky.jvm.gc.thread;

public class ProvideThread  extends Thread implements Runnable{

    private long size;

    public ProvideThread(long size){
        this.size =size;
    }
    @Override
    public void run() {
        while (size >0){
            System.out.println("provider ing ,size is :"+size);
            System.out.println("provider run ,thread name is :"+Thread.currentThread().getName());
            System.out.println("provider , thread status is :"+Thread.currentThread().getState().name());
            try{
                Data data = new Data(Box.COUNT.addAndGet(2L));
                Box.QUEUE.add(data);
                System.out.println("provide : "+data);
                size--;
            }catch (IllegalStateException is){

            }

        }

    }
}
