package org.jerfan.sky.jvm.gc.listener;

import java.util.EventObject;

/**
 * 员工监听事件，在这块可以指定哪些行为需要监听
 * 如下监听了 enter 和 leave 2 个行为
 * 当然也可以分开 进行监听，这里是合并的
 */
public class EmployeeEvent extends EventObject implements EnterCompanyListener,LeaveCompanyListener {


    private AbstractCompanyListener  companyListener;

    /**
     *
     * @param employeeSource 监听对象 赋值给 EventObject.source
     * @param companyListener  AbstractCompanyListener 监听处理器
     */
    public EmployeeEvent(EmployeeSource employeeSource, AbstractCompanyListener companyListener) {
        super(employeeSource);
        this.companyListener = companyListener;
    }

    public EmployeeSource getEmployee() {
        return (EmployeeSource)getSource();
    }

    @Override
    public void enter(EmployeeEvent event) {
        this.getEmployee().enterCompany();
        companyListener.enter(this);
    }

    @Override
    public void leave(EmployeeEvent event) {
        this.getEmployee().leaveCompany();
        companyListener.leave(this);

    }
}
