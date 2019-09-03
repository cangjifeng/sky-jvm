package org.jerfan.sky.jvm.gc.annotations;

import org.springframework.stereotype.Component;

/**
 * @author jerfan.cang
 * @date 2019/9/3  9:32
 */
@Component
public class PromotionOrderFactory implements OrderFactory {
    @Override
    public String createOrder() {
        return "create a promotion order thorough PromotionOrderFactory.class";
    }
}
