java 事件处理机制 ，事件模型 。

### 事件模型几个对象

Source ： 事件源，触发事件的对象；

EventObject ： 事件对象，带了 EventSource 信息的事件对象，是对 EventSource的包装；

Eventlistener ： 事件监听器，对事件的处理。

### 案例

约定所有使用监听模式的接口定义要继承 java.util.EventListener 接口 ，

预定所有监听事件的类都要继承 java.util.EventObject 。

#### EventListener 接口

```
# EventListener
package java.util;
/**
 * A tagging interface that all event listener interfaces must extend.
 * @since JDK1.1
 */
public interface EventListener {
}
```

#### EventObject 接口 

```


# EventObject
package java.util;
/**
 * All Events are constructed with a reference to the object, the "source",
 * that is logically deemed to be the object upon which the Event in question
 * initially occurred upon.
 */
public class EventObject implements java.io.Serializable {
    private static final long serialVersionUID = 5516075349620653480L;
    protected transient Object  source;
    public EventObject(Object source) {
        if (source == null)
            throw new IllegalArgumentException("null source");
        this.source = source;
    }
    public Object getSource() {
        return source;
    }
    public String toString() {
        return getClass().getName() + "[source=" + source + "]";
    }
}

```

#### CompanyListener  事件监听处理器

```
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

```

#### EnterCompanyListener

```
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

```

#### LeaveCompanyListener

```
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

```

#### AbstractCompanyListener 抽象监听处理器

```
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
    public void enter(EmployeeEvent event) {
    }

    @Override
    public void leave(EmployeeEvent event) {

    }
}

```

#### DefaultCompanyListener  监听处理器的默认实现

```
package org.jerfan.sky.jvm.gc.listener;

/**
 * 默认的监听实现
 */
public class DefaultCompanyListener extends  AbstractCompanyListener implements EnterCompanyListener,LeaveCompanyListener ,CompanyListener {

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

```

#### EmployeeEvent 事件对象

```
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

```

#### EmployeeSource  事件源

```
package org.jerfan.sky.jvm.gc.listener;


/**
 * 员工 被监听的对象
 */
public class EmployeeSource {

    private String name;

    public EmployeeSource(String name) {
        this.name = name;
    }


    /**
     * 员工进入公司 会被监听
     */
    public void enterCompany(){
        System.out.println("&&&&&刷卡开门,准备进入");
    }

    /**
     * 员工离开公司 会被监听
     */
    public void leaveCompany(){
        System.out.println("$$$$$感应开门，准备离开 ");
    }

    /**
     * 员工工作 不会被监听
     */
    public void work(){
        System.out.println(name+" is working now ");
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                '}';
    }
}

```

#### EmployeeListenerLoader

```
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

```



补充：

```
org.apache.commons.chain.Command 
org.apache.commons.chain.Chain extends Command
org.apache.commons.chain.impl.ChainBase implements Chain
# 命令模式的使用

<!-- https://mvnrepository.com/artifact/commons-chain/commons-chain -->
<dependency>
    <groupId>commons-chain</groupId>
    <artifactId>commons-chain</artifactId>
    <version>1.2</version>
</dependency>

```



