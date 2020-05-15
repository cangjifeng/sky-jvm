package org.jerfan.sky.jvm.gc.exception;

public class ServerException extends RuntimeException {

    private String code;

    private String message;

    public ServerException(){}

    public ServerException(String code,String message){
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
