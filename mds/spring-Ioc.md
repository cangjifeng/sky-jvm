 spring IOC 实现

spring 容器的启动 ，使用 ApplicationContext 。 它多种实现，ApplicationContext 往下的继承关系，同时  ApplicationContext 

ApplicationContext 到最底层常见的几种实现如下：

ClassPathXmlApplicationContext ；

FileSystemXmlApplicationContext ；

AnnotationConfigApplicationContext (用的多)；

ApplicationContext 类签名

```
package org.springframework.context;

import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.support.ResourcePatternResolver;

public interface ApplicationContext extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory,
		MessageSource, ApplicationEventPublisher, ResourcePatternResolver {
	// 根据 id 获取 bean 在实际中是使用最多的	
	String getId();
	String getApplicationName();
	String getDisplayName();
	long getStartupDate();
	ApplicationContext getParent();
	AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
```

spring 如何通过配置文件或者通过注解来启动 ApplicationContext ，也是 spring IOC 的核心，在 ApplicationContext 启动过程中会实例化 bean 和往 bean 中注入依赖。实例化 bean 要使用到 BeanFactory 。HierarchicalBeanFactory 、 ListableBeanFactory 、AutowireCapableBeanFactory 继承了 BeanFactory 。从 ClassPathXmlApplicationContext 的构造方法入手看。

```
#ClassPathXmlApplicationContext 构造方法签名
public ClassPathXmlApplicationContext() {
	}
#使用父类的ApplicationContext
public ClassPathXmlApplicationContext(ApplicationContext parent) {
	}
# 使用给定的url构造 ApplicationContext
public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[] {configLocation}, true, null);
	}
# 使用给定的url创建新的 ApplicationContext , refresh() 方法很重要
public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, 
                         ApplicationContext parent) throws BeansException {
		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
			refresh();
		}
	}

```

refresh()  方法 ，bean 在 spring 容器中的底层存储使用的Map

```
public abstract class AbstractApplicationContext extends DefaultResourceLoader
		implements ConfigurableApplicationContext, DisposableBean {
		
		@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// Prepare this context for refreshing.
			// 记录启动时间和启动状态 (类型是 AtomicBoolean) closed =false , active = true 
			prepareRefresh();

			// Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				// Invoke factory processors registered as beans in the context.
				invokeBeanFactoryPostProcessors(beanFactory);

				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				// Initialize message source for this context.
				initMessageSource();

				// Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// Initialize other special beans in specific context subclasses.
				onRefresh();

				// Check for listener beans and register them.
				registerListeners();

				// Instantiate all remaining (non-lazy-init) singletons.
				finishBeanFactoryInitialization(beanFactory);

				// Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}
}
```

ConfigurableListableBeanFactory  有一个实现类 DefaultListableBeanFactory ， DefaultListableBeanFactory 是最牛的了。BeanFactory 是 bean的容器， bean  则是使用 BeanDefinition 表示。

```
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
		implements ConfigurableListableBeanFactory, BeanDefinitionRegistry, Serializable {
        // 存储定义的 bean 底层是 Map ，使用哪个的是 ConcurrentHashMap 
        private final Map<String, BeanDefinition> beanDefinitionMap = 
        new ConcurrentHashMap<String, BeanDefinition>(256);
}
```

BeanDefinition ，我们在 xml 中,使用注解等定义的 bean 会转换成 BeanDefinition ，并且存在是 spring 容器中（即存在在 BeanFactory 中）。重点关注 BeanDefinition 中 的 bean 类型，常用的 是 单例和原型，其他的都是基于WEB的扩展。

```
package org.springframework.beans.factory.config;

import org.springframework.beans.BeanMetadataElement;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.AttributeAccessor;

/**
 * A BeanDefinition describes a bean instance, which has property values,
 * constructor argument values, and further information supplied by
 * concrete implementations.
 *
 * <p>This is just a minimal interface: The main intention is to allow a
 * {@link BeanFactoryPostProcessor} such as {@link PropertyPlaceholderConfigurer}
 * to introspect and modify property values and other bean metadata.
 *
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @since 19.03.2004
 * @see ConfigurableListableBeanFactory#getBeanDefinition
 * @see org.springframework.beans.factory.support.RootBeanDefinition
 * @see org.springframework.beans.factory.support.ChildBeanDefinition
 */
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {

	/**
	 * Scope identifier for the standard singleton scope: "singleton".
	 * 单例的 bean 
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 */
	String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

	/**
	 * Scope identifier for the standard prototype scope: "prototype".
	 * 原型的 bean
	 * <p>Note that extended bean factories might support further scopes.
	 * @see #setScope
	 */
	String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;
	
	int ROLE_APPLICATION = 0;
	int ROLE_SUPPORT = 1;
	int ROLE_INFRASTRUCTURE = 2;
	String getParentName();
	void setParentName(String parentName);
	String getBeanClassName();
	void setBeanClassName(String beanClassName);
	String getFactoryBeanName();
	void setFactoryBeanName(String factoryBeanName);
	String getFactoryMethodName();
	void setFactoryMethodName(String factoryMethodName);
	String getScope();
	void setScope(String scope);
	boolean isLazyInit();
	void setLazyInit(boolean lazyInit);
	String[] getDependsOn();
	// bean 依赖 
	void setDependsOn(String... dependsOn);
	boolean isAutowireCandidate();
	void setAutowireCandidate(boolean autowireCandidate);
	boolean isPrimary();
	void setPrimary(boolean primary);
	ConstructorArgumentValues getConstructorArgumentValues();
	MutablePropertyValues getPropertyValues();
	boolean isSingleton();
	boolean isPrototype();
	boolean isAbstract();
	int getRole();
	String getDescription();
	String getResourceDescription();
	BeanDefinition getOriginatingBeanDefinition();

}

```

bean 注入 有覆盖问题（同一个配置文件中，会抛异常，不同文件中会覆盖），循环依赖问题（A 依赖B ，B 依赖C ，但是在构造函数中出现A 依赖B 和B依赖A 是不行的）这个配置是在 customizeBeanFactory 方法中实现。回头看 refresh() 方法中的，obtainFreshBeanFactory()  方法 ，这个方法完成初始化  BeanFactory  、 加载 Bean  、 注册 Bean 等操作。
AbstractRefreshableApplicationContext 继承了 AbstractApplicationContext 。AbstractXmlApplicationContext 加载和解析 xml 的资源信息，会使用到 AbstractBeanDefinitionReader 类 ，XmlBeanDefinitionReader 继承了 AbstractBeanDefinitionReader 。

```
# AbstractApplicationContext.obtainFreshBeanFactory() 方法签名
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
		refreshBeanFactory();
		ConfigurableListableBeanFactory beanFactory = getBeanFactory();
		if (logger.isDebugEnabled()) {
			logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
		}
		return beanFactory;
	}
```

```
# AbstractRefreshableApplicationContext.refreshBeanFactory() 方法签名
protected final void refreshBeanFactory() throws BeansException {
		if (hasBeanFactory()) {
			destroyBeans();
			closeBeanFactory();
		}
		try {
		    // DefaultListableBeanFactory 正在使用的 类
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			beanFactory.setSerializationId(getId());
			// 配置是否允许 BeanDefinition 覆盖、是否允许循环引用。
			customizeBeanFactory(beanFactory);
			loadBeanDefinitions(beanFactory);
			synchronized (this.beanFactoryMonitor) {
				this.beanFactory = beanFactory;
			}
		}
		catch (IOException ex) {
			throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
		}
	}
```

```
# customizeBeanFactory 方法签名
protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
		if (this.allowBeanDefinitionOverriding != null) {
			beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
		}
		if (this.allowCircularReferences != null) {
			beanFactory.setAllowCircularReferences(this.allowCircularReferences);
		}
	}
```

```
# AbstractXmlApplicationContext.loadBeanDefinitions 方法签名
protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// Configure the bean definition reader with this context's
		// resource loading environment.
		beanDefinitionReader.setEnvironment(this.getEnvironment());
		beanDefinitionReader.setResourceLoader(this);
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		initBeanDefinitionReader(beanDefinitionReader);
		loadBeanDefinitions(beanDefinitionReader);
	}
# AbstractXmlApplicationContext.loadBeanDefinitions 方法签名
protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		Resource[] configResources = getConfigResources();
		if (configResources != null) {
			reader.loadBeanDefinitions(configResources);
		}
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			reader.loadBeanDefinitions(configLocations);
		}
	}
```

```
#AbstractBeanDefinitionReader.loadBeanDefinitions 方法签名
public int loadBeanDefinitions(String... locations) throws BeanDefinitionStoreException {
		Assert.notNull(locations, "Location array must not be null");
		int counter = 0;
		for (String location : locations) {
			counter += loadBeanDefinitions(location);
		}
		return counter;
	}
```

```
# XmlBeanDefinitionReader.loadBeanDefinitions 方法签名
public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(new EncodedResource(resource));
	}
# XmlBeanDefinitionReader.loadBeanDefinitions 方法签名
public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
		Assert.notNull(encodedResource, "EncodedResource must not be null");
		if (logger.isInfoEnabled()) {
			logger.info("Loading XML bean definitions from " + encodedResource.getResource());
		}

		Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();
		if (currentResources == null) {
			currentResources = new HashSet<EncodedResource>(4);
			this.resourcesCurrentlyBeingLoaded.set(currentResources);
		}
		if (!currentResources.add(encodedResource)) {
			throw new BeanDefinitionStoreException(
					"Detected cyclic loading of " + encodedResource + " - check your import definitions!");
		}
		try {
			InputStream inputStream = encodedResource.getResource().getInputStream();
			try {
				InputSource inputSource = new InputSource(inputStream);
				if (encodedResource.getEncoding() != null) {
					inputSource.setEncoding(encodedResource.getEncoding());
				}
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
			}
			finally {
				inputStream.close();
			}
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(
					"IOException parsing XML document from " + encodedResource.getResource(), ex);
		}
		finally {
			currentResources.remove(encodedResource);
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();
			}
		}
	}
```



参考：
 1 https://blog.csdn.net/nuomizhende45/article/details/81158383 