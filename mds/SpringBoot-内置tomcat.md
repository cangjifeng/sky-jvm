**SpringBoot 内置tomcat 和 SpringBoot 工程的启动说明**

**重点了解的内容 ：**
1 SpringApplication.构造函数 和 run() 方法
2 ConfigurableApplicationContext 是构造过程
3 ApplicationContextInitializer
4 ApplicationListener
5 spring 上下文

SpringBoot 构建web工程引入的maven 如下，其中 spring-boot-starter-web 中依赖了 tomcat ，如果要不使用 SpringBoot 内置的 tomcat ，需要把 tomcat 依赖的jar 排除，并把工程打成war，使用tomcat 部署 需要引入 ，并更新main函数，使其继承 SpringBootServletInitializer，并重写configure()方法。

```java
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
   <version>2.1.6.RELEASE</version>
   <!-- 移除嵌入式tomcat插件 -->
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>
<!--添加servlet-api依赖--->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>3.1.0</version>
</dependency>
```

```
#启动 SpringBoot 工程 执行 ，关键的地方是使用 @SpringBootApplication 注解 和 SpringApplication.run(Class clazz) 方法。
@SpringBootApplication
public class MySpringbootTomcatStarter{
    public static void main(String[] args) {
        SpringApplication.run(MySpringbootTomcatStarter.class);
    }
}
# 更新main 函数 继承 SpringBootServletInitializer 并重写 configure() 方法
@SpringBootApplication
public class MySpringbootTomcatStarter extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MySpringbootTomcatStarter.class);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(this.getClass());
    }
}
```

SpringBoot 工程的 main函数 中的 SpringApplication.run()方法返回的 是 ConfigurableApplicationContext ，ConfigurableApplicationContext<interface> 继承了 ConfigurableApplicationContext  

```
#SpringApplication.run 方法签名
public static ConfigurableApplicationContext run(Class<?> primarySource, String... args) {
		return run(new Class<?>[] { primarySource }, args);
	}

# ConfigurableApplicationContext 类申明 ，其中 org.springframework.context.ApplicationContext 是 pring工程中的
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {
 
}
# SpringApplication 构造函数地方使用的 是 ApplicationContextInitializer 和 ApplicationListener
public SpringApplication(ResourceLoader resourceLoader, Class<?>... primarySources) {
		this.resourceLoader = resourceLoader;
		Assert.notNull(primarySources, "PrimarySources must not be null");
		this.primarySources = new LinkedHashSet<>(Arrays.asList(primarySources));
		this.webApplicationType = WebApplicationType.deduceFromClasspath();
		setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
		setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
		this.mainApplicationClass = deduceMainApplicationClass();
	}
# ApplicationContextInitializer 类申明
public interface ApplicationContextInitializer<C extends ConfigurableApplicationContext> {

	/**
	 * Initialize the given application context.
	 * @param applicationContext the application to configure
	 */
	void initialize(C applicationContext);

}
# ApplicationListener 类声明
@FunctionalInterface
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	/**
	 * Handle an application event.
	 * @param event the event to respond to
	 */
	void onApplicationEvent(E event);

}
```




