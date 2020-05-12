package org.jerfan.sky.jvm.gc.listener;

/**
 * 离开公司事件监听
 */
public interface LeaveCompanyListener extends CompanyListener{

    /**
     * 监听离开公司
     * @param event EmployeeEvent
     */
    void leave(EmployeeEvent event);
}
