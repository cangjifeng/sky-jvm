package org.jerfan.sky.jvm.gc.lock;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FactoryManagerExecutor {

    public static void main(String[] args) {
        System.out.println("begin");
        ProducerFactory factory = new ProducerFactory();
        ExecutorService executorService = Executors.newFixedThreadPool (2);

        executorService.execute(new Thread( () -> {
            factory.saleComponentTask();
        }));
        executorService.execute(new Thread( () -> {
            factory.handleTask();
        }));
        executorService.execute(new Thread( () -> {
            factory.acceptTask(10,"叮当猫");
        }));


        executorService.execute(new Thread( () -> {
            factory.acceptTask(10,"小汽车");
        }));
        executorService.execute(new Thread( () -> {
            factory.handleTask();
        }));
        executorService.execute(new Thread( () -> {
            factory.saleComponentTask();
        }));



        System.out.println("end");
    }
}
