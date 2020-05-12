package org.jerfan.sky.jvm.gc.listener;

/**
 * 进入公司事件监听
 */
public interface EnterCompanyListener extends CompanyListener{

    /**
     * 进入公司事件会被监听
     * @param event EmployeeEvent
     */
    void enter(EmployeeEvent event);
}
