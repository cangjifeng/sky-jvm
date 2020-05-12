package org.jerfan.sky.jvm.gc.listener;

import java.util.EventListener;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 公司监听处理器
 * 具体要监听哪些事件 需要自定义并实现 CompanyListener接口
 */
public interface CompanyListener extends EventListener {
    // 当员工进入公司 则添加到map，当员工离开从map中移除
    Map<String, EmployeeSource> EMPLOYEE_MAP = new ConcurrentHashMap();

    // 监控履历
    List<String> HISTORY_LIST = new LinkedList<>();

}
