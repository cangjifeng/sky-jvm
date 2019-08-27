package org.jerfan.sky.jvm.gc;

/**
 * @author jerfan.cang
 * @date 2019/8/26 10:31
 */
public class FinalizeEscapeGC {

    public static FinalizeEscapeGC SAVE_HOOK = null;

    public void isAlive(){
        System.out.println("still alive ...");
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("execute finalize method");
        FinalizeEscapeGC.SAVE_HOOK = this;
    }

    public static void main(String[] args) throws Exception{
        SAVE_HOOK = new FinalizeEscapeGC();

        SAVE_HOOK = null;
        System.gc();
        System.out.println("the first : ");
        Thread.sleep(500);
        if( null != SAVE_HOOK){
            SAVE_HOOK.isAlive();
        }else{
            System.out.println("had dead");
        }
        System.out.println("the second : ");
        SAVE_HOOK = null;
        System.gc();
        Thread.sleep(500);
        if( null != SAVE_HOOK){
            SAVE_HOOK.isAlive();
        }else{
            System.out.println("had dead");
        }
    }
}
