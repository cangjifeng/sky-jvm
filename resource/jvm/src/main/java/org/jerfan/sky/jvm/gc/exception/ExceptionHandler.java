package org.jerfan.sky.jvm.gc.exception;

import java.time.LocalDate;

public class ExceptionHandler {


    public static void main(String[] args) {
        System.out.println("start");
        ExceptionHandler handler = new ExceptionHandler();
        try{
            String value="南京-雨花台区-";
            handler.param(value);
            handler.innerHandle(value);
        }catch (ParamException pe){
            System.out.println(pe);
        }catch (ServerException se){
            System.out.println(se);
        }catch (InnerException ie){
            System.out.println(ie);
        }catch (Exception e){
            System.out.println(e);
        }
        System.out.println("shutdown");
    }

    public void param(String value){
        if(null == value || "".equals(value)){
            throw new ParamException("-1","value can not be empty.");
        }
    }

    public String innerHandle(String value){

        StringBuilder builder = new StringBuilder(value);
        try{
            System.out.println("inner handle value:"+value+",处理时间："+LocalDate.now().toString());

            int x = 10/0;
        }catch (Exception e){
            throw new InnerException("-3","内部异常");
        }
        return builder.toString();
    }

    public String server(String value,int size){
        if(size < 10){
            throw new ServerException("-2","服务异常");
        }
        return value.substring(0,10)+size;
    }
}
