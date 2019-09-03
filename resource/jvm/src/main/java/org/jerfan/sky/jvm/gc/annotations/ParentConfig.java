package org.jerfan.sky.jvm.gc.annotations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author jerfan.cang
 * @date 2019/9/3  9:35
 */
@Configuration
@Import({CommonOrderFactoryConfig.class, PromotionOrderFactoryConfig.class})
public class ParentConfig {
}
