package org.jerfan.sky.jvm.gc.annotations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jerfan.cang
 * @date 2019/9/3  9:34
 */
@Configuration
public class PromotionOrderFactoryConfig {

    @Bean(name = "promotionOrderFactory")
    public OrderFactory getToyota(){
        System.out.println("init promotionOrderFactory");
        return  new PromotionOrderFactory();
    }
}
