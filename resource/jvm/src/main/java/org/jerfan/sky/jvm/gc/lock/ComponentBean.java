package org.jerfan.sky.jvm.gc.lock;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ComponentBean {

    /**
     * 产品名称
     */
    private String name;

    /**
     * sn 编号
     */
    private String sn;

    /**
     * 生产日期
     */
    private LocalDate releaseTime;



    /**
     * 订货日期
     */
    private LocalDate orderTime;

    public ComponentBean(){}

    public ComponentBean(String name){
        this.name = name;
        orderTime = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        sn = orderTime.format(formatter)+System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public LocalDate getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(LocalDate releaseTime) {
        this.releaseTime = releaseTime;
    }

    public LocalDate getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDate orderTime) {
        this.orderTime = orderTime;
    }

    @Override
    public String toString() {
        return "ComponentBean{" +
                "name='" + name + '\'' +
                ", sn='" + sn + '\'' +
                ", releaseTime=" + releaseTime +
                ", orderTime=" + orderTime +
                '}';
    }
}
