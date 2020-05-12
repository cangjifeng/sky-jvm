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
