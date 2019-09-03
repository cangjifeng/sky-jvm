package org.jerfan.sky.jvm.gc.annotations;

import org.springframework.stereotype.Component;

/**
 * @author jerfan.cang
 * @date 2019/9/3  9:31
 */
@Component
public class CommonOrderFactory implements OrderFactory {

    @Override
    public String createOrder() {
        return "create common order through CommonOrderFactory.class ";
    }
}
