package org.jerfan.sky.jvm.gc.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataLoader {


    public static void main(String[] args) throws Exception{

        System.out.println("ready go");
        ExecutorService executorService = Executors.newFixedThreadPool (10);
        Thread provide1 = new ProvideThread(1000);
        provide1.setName("生产1号");


        System.out.println("线程名称"+provide1.getName());
        System.out.println("线程状态"+provide1.getState().name());
        //executorService.execute(provide1);
        provide1.wait();

        System.out.println("主动让挂起线程-等待，线程名称"+provide1.getName());
        System.out.println("线程名称"+provide1.getName());
        System.out.println("线程状态"+provide1.getState().name());
        provide1.start();

        synchronized (provide1){
            provide1.wait();
        }
        Thread.sleep(6000);
        System.out.println("线程名称"+provide1.getName());
        System.out.println("线程状态"+provide1.getState().name());
        Thread.sleep(3000);
        System.out.println("线程名称"+provide1.getName());
        System.out.println("线程状态"+provide1.getState().name());
        System.out.println("shutdown");

    }
}
