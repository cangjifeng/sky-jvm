package org.jerfan.sky.jvm.gc.exception;

public class ParamException extends RuntimeException{

    private String code;

    private String message;

    public ParamException(){}

    public ParamException(String code,String message){
        this.code=code;
        this.message=message;
    }


    @Override
    public String toString() {
        return "ParamException{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
