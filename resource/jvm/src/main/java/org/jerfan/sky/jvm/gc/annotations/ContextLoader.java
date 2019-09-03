package org.jerfan.sky.jvm.gc.annotations;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author jerfan.cang
 * @date 2019/9/3  9:36
 */
public class ContextLoader {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ParentConfig.class);

        OrderFactory orderFactory =(CommonOrderFactory)context.getBean("commonOrderFactory");
        String message = orderFactory.createOrder();
        System.out.println(message);
        orderFactory = (PromotionOrderFactory) context.getBean("promotionOrderFactory");
        message = orderFactory.createOrder();
        System.out.println(message);
        System.out.println("end");
    }
}
