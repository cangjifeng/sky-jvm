java锁

### java中的使用的锁介绍

​        java中介绍的锁，不是按照锁的类型。有的是按照锁的状态，有的是按照锁的特性，有的是按照锁的实现方式等。

- 乐观锁/悲观锁
- 独占锁/共享锁
- 互斥锁/读写锁
- 可重入锁
- 公平锁/非公平锁
- 分段锁
- 偏向锁、轻量级锁、重量级锁
- 自旋锁



#### 乐观锁/悲观锁**

​        从看待并发角度分析，是否会引起并发问题，分乐观锁和悲观锁。

**乐观锁**： 

​        乐观锁总是认为不存在并发问题，每次去取数据的时候，总认为不会有其他线程对数据进行修改，因此不会上锁。但是在更新时会判断其他线程在这之前有没有对数据进行修改，一般会使用“数据版本机制”或“CAS操作”来实现。

**悲观锁：**

​         悲观锁认为对于同一个数据的并发操作，一定会发生修改的，哪怕没有修改，也会认为修改。因此对于同一份数据的并发操作，悲观锁采取加锁的形式。悲观的认为，不加锁并发操作一定会出问题。

#### **独占锁/共享锁**

​         独占锁指该锁一次只能被一个线程所持有，而共享锁一次可以被多个线程所持有。独占锁和共享锁通过 AQS 实现，Synchronized 也被归属在独占锁中。

#### 互斥锁/读写锁

​        独占锁/共享锁，是从广义上的说法，而互斥锁/读写锁则是具体的实现，如 ReentrantLock 是互斥锁，ReadWriteLock 是读写锁。

#### 可重入锁

​        可重入锁又叫递归锁，是指一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁。可重入锁可以避免死锁。Synchronized 也是一种可重入锁。

#### 公平锁/非公平锁

​        公平锁是按照申请锁的顺序来获取锁，而非公平锁则不是按照申请顺序。可能引发优先级顺序反转或者饥饿现象。非公平锁的吞吐量大，Synchronized 是非公平锁，ReetrantLock 可以指定其是使用公平锁还是非公平锁，默认是非公平锁。

#### 分段锁

​        分段锁是一种锁的设计，它并不是一种锁。分段锁设计的目的是细化锁的粒度。ConcurrentHashMap 是使用分段锁方式实现的。

#### 偏向锁/轻量级锁/重量级锁

​        这三种锁是指锁的状态，并且是针对Synchronized。在Java 5通过引入锁升级的机制来实现高效Synchronized。这三种锁的状态是通过对象监视器在对象头中的字段来表明的。

​        偏向锁是指一段同步代码一直被一个线程所访问，那么该线程会自动获取锁。降低获取锁的代价。

　　轻量级锁是指当锁是偏向锁的时候，被另一个线程所访问，偏向锁就会升级为轻量级锁，其他线程会通过自旋的形式尝试获取锁，不会阻塞，提高性能。

　　重量级锁是指当锁为轻量级锁的时候，另一个线程虽然是自旋，但自旋不会一直持续下去，当自旋一定次数的时候，还没有获取到锁，就会进入阻塞，该锁膨胀为重量级锁。重量级锁会让他申请的线程进入阻塞，性能降低。

自旋锁

​        自旋锁是指在java中获取锁的线程不会立即被阻塞，而是采用循环的方式去尝试获取锁。可以减少上下文切换的消耗，但会增加CPU的消耗（因为一直在循环，知道获取到锁）。



### 补充

#### CAS :

​           compare and  swap ，比较并交换 。乐观锁的一种实现方式，如java.util.concurrent.atomic包下面的原子变量类。

synchronized ： 实现了悲观锁 ，也是实现了独占锁。

#### AQS ： 

​          Abstract Queued Synchronizer 抽象队列同步器，简称 AQS, 它提供了一套多线程访问共享资源的同步器框架。ReentrantLock、Semaphore、CountDownLatch、CyclicBarrier等并发类均是基于AQS来实现的，使用的设计模式是模板方法 。AQS 维护一个 volatile int status (代表共享资源)和一个FIFO线程等待队列（多线程争用资源被阻塞时会进入此队列）。

​           在 juc 的 locks 包路径下。

```
# 在 juc 的 locks 包路径下
AbstractOwnableSynchronizer #<interface>
AbstractQueuedSynchronizer extends AbstractOwnableSynchronizer #<abstract class>
Node #<static final class> in AbstractQueuedSynchronizer

```

juc : java.util.concurrent 包的简称。

#### Condition：

​          java.util.concurrent.locks.Condition  ，Condition是在java 1.5中才出现的，它用来替代传统的Object的wait()、notify()实现线程间的协作，相比使用Object的wait()、notify()，使用Condition的await()、signal()这种方式实现线程间协作更加安全和高效。因此通常来说比较推荐使用Condition，阻塞队列实际上是使用了Condition来模拟线程间协作。

- Condition是个接口，基本的方法就是await()和signal()方法;

- Condition依赖于Lock接口，生成一个Condition的基本代码是lock.newCondition() ;

- 调用Condition的await()和signal()方法，都必须在lock保护之内，就是说必须在lock.lock()和lock.unlock之间才可以使用

  Condition中的await()对应Object的wait()；

  Condition中的signal()对应Object的notify()；

  Condition中的signalAll()对应Object的notifyAll()。

```
 Lock lock = new ReentrantLock();  
 Condition condition = lock.newCondition(); 
```

实现同步方式：

1. ​        使用 synchronized 关键字 并配合 Object.wait()  、 Object.notify()  、  Object.notifyAll() 一系列方法实现等待/通知模式。
2. ​        使用 Lock ，Condition 对象是依赖 Lock 的 , 通过 Lock.newCondition() 方法创建 Condition 对象，

java并发编程艺术中对比 Condition 和 Object

| 对比项                                                | Object monitor method          | condition                                                    |
| :---------------------------------------------------- | :----------------------------- | ------------------------------------------------------------ |
| 前置条件                                              | 获取对象的锁                   | 调用 Lock.lock() 获取锁<br />调用 Lock.newCondition() 获取 Condition 对象 |
| 调用方式                                              | 直接调用<br />如 object.wait() | 直接调用<br />如 condition.wait()                            |
| 等待队列个数                                          | 1个                            | N个                                                          |
| 当前线程释放锁并进入等待                              | 支持                           | 支持                                                         |
| 当前线程释放锁并进入等待,<br />在等待状态中不响应中断 | 不支持                         | 支持                                                         |
| 当线程释放锁并进入超时等待状态                        | 支持                           | 支持                                                         |
| 当前线程释放锁并进入等待状态<br />到将来的摸个时间    | 不支持                         | 支持                                                         |
| 唤醒等待队列中的一个线程                              | 支持                           | 支持                                                         |
| 唤醒等待队列中的全部线程                              | 支持                           | 支持                                                         |





#### java 线程的状态

```
public enum State {
        /**
        * 线程刚创建，但是还没有启动
         */
        NEW,

        /**
        * 线程已经启动，也叫运行中，但是可能因其他资源进入等待状态
         */
        RUNNABLE,

        /**
        * 阻塞状态
         */
        BLOCKED,

        /**
         * 等待状态
         */
        WAITING,

        /**
         * 超时等待，当倒计时结束切换到运行状态
         */
        TIMED_WAITING,

        /**
         * 已完成，已结束
         */
        TERMINATED;
    }
```

x

