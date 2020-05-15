package org.jerfan.sky.jvm.gc.thread;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public interface Box {

    BlockingQueue<Data> QUEUE = new ArrayBlockingQueue(1000);


     AtomicLong COUNT = new AtomicLong(10L);
}
