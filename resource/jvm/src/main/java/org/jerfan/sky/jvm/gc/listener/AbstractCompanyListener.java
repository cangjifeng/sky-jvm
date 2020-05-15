package org.jerfan.sky.jvm.gc.listener;

import java.time.LocalDateTime;

/**
 * 进入和离开公司事件抽象监听
 */
public abstract class AbstractCompanyListener  implements EnterCompanyListener,LeaveCompanyListener{

    public void register(EmployeeSource employeeSource){
        LocalDateTime localDateTime = LocalDateTime.now();
        EMPLOYEE_MAP.put(employeeSource.getName(), employeeSource);
        StringBuilder builder = new StringBuilder();
        builder.append("----- \n欢迎").append(employeeSource.getName()).append("进入公司!");
        builder.append("\n当前时间：").append(localDateTime.toString());
        builder.append("。\n当前公司员工信息如下：\n").append(EMPLOYEE_MAP.toString()).append("\n-----");
        System.out.println(builder.toString());
        HISTORY_LIST.add(builder.toString());
    }

    public void destroy(EmployeeSource employeeSource){
        LocalDateTime localDateTime = LocalDateTime.now();
        EMPLOYEE_MAP.remove(employeeSource.getName());
        StringBuilder builder = new StringBuilder();
        builder.append(" ***** \n 再见").append(employeeSource.getName()).append(",欢迎下次光临！");
        builder.append("\n当前时间：").append(localDateTime.toString());
        builder.append("。\n当前公司员工信息如下：\n").append(EMPLOYEE_MAP.toString()).append("\n***** ");
        System.out.println(builder.toString());
        HISTORY_LIST.add(builder.toString());

    }

    @Override
    public abstract void enter(EmployeeEvent event) ;

    @Override
    public abstract void leave(EmployeeEvent event) ;
}
