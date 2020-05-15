package org.jerfan.sky.jvm.gc.lock;

import java.util.ArrayList;
import java.util.List;

public class Warehouse {


    /**
     * 生产任务列表
     */
    public static volatile List<ComponentBean> taskList = new ArrayList<>();


    /**
     * 成品列表
     */
    public static volatile List<ComponentBean> releaseList = new ArrayList<>();


}
