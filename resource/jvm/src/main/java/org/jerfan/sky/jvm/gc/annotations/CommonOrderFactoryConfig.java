package org.jerfan.sky.jvm.gc.annotations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jerfan.cang
 * @date 2019/9/3  9:32
 */
@Configuration
public class CommonOrderFactoryConfig {

    @Bean(name = "commonOrderFactory")
    public OrderFactory getVolkswagen(){
        System.out.println("init commonOrderFactory");
        return  new CommonOrderFactory();
    }
}
