**@Import 注解详情**

# bean注入到spring容器的方式

## xml配置

​        xml中使用<bean>标签配置

## @Bean注解配置

​        使用@Configuration 、@Component  、和 @Bean 注解，@Configuration 注解继承了@Component 注解，如果在一个类中使用 @Configuration 和 @Bean 。注入方式可以参考下面的实现。如果需要配置的bean很多，需要对其进行分类，可以把注入配置写在多个class中，然后给出一个汇总的配置，在这个汇总的配置类里需要引入其他的注入配置，这个和xml的<import>标签 是一样的，使用的是@Import 注解。

```java
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
```

# @Import  注解的使用方式

​        @Import 注解使用方式如下，它完成的任务是把分散的bean注入配置集中到一起。在项目启动时候一次性注入。

```java
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

```

orderFactory接口代码

```java
package org.jerfan.sky.jvm.gc.annotations;

public interface OrderFactory {

    String createOrder();
}
```

commonOrderFactory 实现了 orderFactory 接口

```java
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

```

promotionOrderFactory 类实现了 orderFactory 接口

```java
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

```

# 测试@Import 使用

下面是测试的结果，根据执行结果可以看出集中注入是成功的。

```java
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

```

执行结果信息

```
init promotionOrderFactory
create common order through CommonOrderFactory.class 
create a promotion order thorough PromotionOrderFactory.class
end
```

# @Import 原理

上面是@Import 注解的使用，该注解是怎么玩的，往下面看。

@Import 注解定义的源码

```java
package org.springframework.context.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {

	/**
	 * {@link Configuration}, {@link ImportSelector}, {@link ImportBeanDefinitionRegistrar}
	 * or regular component classes to import.
	 */
	Class<?>[] value();

}
```



@Import 只能作用在类上（class 、 interface 、enum）；

@Import 的值是数组，value{xx.class,xxx.class}

@Import 的作用是用于导入指定的类，默认组件id是组件的全类名，上面ParentConfig类的注入是 “org.jerfan.sky.jvm.gc.annotations.ParentConfig” 。



## @Import 注入原理

底层是通过 org.springframework.context.annotation.ConfigurationClassPostProcessor 后置处理器来注入的，核心方法是 postProcessBeanDefinitionRegistry() 。



```java
@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		int registryId = System.identityHashCode(registry);
		if (this.registriesPostProcessed.contains(registryId)) {
			throw new IllegalStateException(
					"postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);
		}
		if (this.factoriesPostProcessed.contains(registryId)) {
			throw new IllegalStateException(
					"postProcessBeanFactory already called on this post-processor against " + registry);
		}
		this.registriesPostProcessed.add(registryId);

		processConfigBeanDefinitions(registry);
	}
```

## @Import 注解 value 中类的注入方式

###         注入分类有3种

1. 普通类的注入
2. 实现了 ImportSelector 接口类的注入
3. 实现了 ImportBeanDefinitionRegistrar 接口类的注入



