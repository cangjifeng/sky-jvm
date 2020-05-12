package org.jerfan.sky.jvm.gc.listener;

import java.util.Arrays;
import java.util.List;

public class EmployeeListenerLoader {

    public static void main(String[] args) {

        // 事件监听处理器
        AbstractCompanyListener listener = new DefaultCompanyListener();

        // 监听的对象 也叫事件源
        EmployeeSource rose = new EmployeeSource("li");
        EmployeeSource ala = new EmployeeSource("ala");

        // 需要监听的事件,也叫事件对象
        EmployeeEvent liEvent = new EmployeeEvent(rose,listener);
        EmployeeEvent alaEvent = new EmployeeEvent(ala,listener);

        List<EmployeeEvent> employeeList = Arrays.asList(liEvent,alaEvent);
        employeeList.forEach( x ->{
            x.enter(x);
        });
        alaEvent.leave(alaEvent);

        System.out.println("end");

    }
}
