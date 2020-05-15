package org.jerfan.sky.jvm.gc.listener;

/**
 * 默认的监听实现
 */
public class DefaultCompanyListener extends  AbstractCompanyListener implements EnterCompanyListener,LeaveCompanyListener  {

    @Override
    public void enter(EmployeeEvent event) {
        System.out.println("嘀嘀嘀 监控器发现情况："+event.getEmployee().getName()+" 进入了公司。");
        register(event.getEmployee());
    }

    @Override
    public void leave(EmployeeEvent event) {
        System.out.println("嘀嘀嘀 监控器发现情况："+event.getEmployee().getName()+" 离开了公司。");
        destroy(event.getEmployee());
    }
}
