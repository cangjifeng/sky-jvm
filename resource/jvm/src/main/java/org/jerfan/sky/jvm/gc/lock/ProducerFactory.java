package org.jerfan.sky.jvm.gc.lock;

import java.time.LocalDate;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生常者
 */
public class ProducerFactory {


    private Lock lock = new ReentrantLock();
    private Condition taskCondition= lock.newCondition();
    private Condition releaseCondition = lock.newCondition();
    private Condition saleCondition = lock.newCondition();


    /**
     * 订货数量
     * @param orderSize int
     * @param name 零件名称
     */
    public void acceptTask(int orderSize,String name){
        lock.lock();
        System.out.println("acceptTask 获取锁成功");
        try{
            while (orderSize >0){
                ComponentBean component = new ComponentBean(name);
                Warehouse.taskList.add(component);
                System.out.println("收到生产任务，纳入生产计划："+component);
                orderSize--;
                releaseCondition.signal();
                System.out.println("acceptTask 发出信号 唤醒生产");
            }

        }catch (Exception e){
            System.out.println(e);
        }finally {
            lock.unlock();
            System.out.println("acceptTask 释放锁");
        }



    }


    /**
     * 生产数量
     */
    public void handleTask(){

        lock.lock();
        System.out.println("handleTask 获取锁成功");
        try{

                System.out.println("handleTask ing");
                if(Warehouse.taskList.isEmpty()){
                    System.out.println("没有生产任务 进入等待");
                    taskCondition.notify();
                    releaseCondition.await();
                }else{
                    ComponentBean component = Warehouse.taskList.get(0);
                    component.setReleaseTime(LocalDate.now());
                    System.out.println("生产零件："+component);
                    Warehouse.releaseList.add(component);
                    Warehouse.taskList.remove(0);
                    saleCondition.notify();
                    if(20==Warehouse.releaseList.size()){
                        System.out.println("零件堆积，暂停生产");
                        releaseCondition.await();
                    }
                }


        }catch (Exception e){
            System.out.println(e);
        }finally {
            lock.unlock();
            System.out.println("handleTask 释放锁");
        }



    }


    public void saleComponentTask(){
        lock.lock();
        System.out.println("saleComponentTask 获取锁成功");
        try{


                if(!Warehouse.releaseList.isEmpty()){
                    ComponentBean component = Warehouse.releaseList.get(0);
                    Warehouse.releaseList.remove(0);
                    System.out.println("业务员销售零件成功："+component);

                    if(Warehouse.releaseList.size() <10){
                        releaseCondition.notify();
                        System.out.println("通知生产");
                    }
                }else{
                    saleCondition.await();
                    System.out.println("saleTask 等待");
                }





        }catch (Exception e){
            System.out.println(e);
        }finally {
            lock.unlock();
            System.out.println("saleComponentTask 释放锁");
        }
    }
}
