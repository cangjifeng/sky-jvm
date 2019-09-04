package org.jerfan.sky.jvm.gc.dozer;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author jerfan.cang
 * @date 2019/9/3  15:17
 */
@Configuration
public class DozerConfig {

    @Bean(value = "dozerMapper")
    public Mapper initMapper(){
        return  DozerBeanMapperBuilder.create().build(); //6.0.0 版本
        //return new DozerBeanMapper(); // 5.5.1 版本
    }
}
