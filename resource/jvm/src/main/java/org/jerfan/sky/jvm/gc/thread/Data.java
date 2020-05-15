package org.jerfan.sky.jvm.gc.thread;

public class Data {

    private Long code;

    public Data(){}

    public Data(Long code){
        this.code=code;
    }

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Data{" +
                "code=" + code +
                '}';
    }
}
