mysql 事务和 spring 事务管理

## 事务的基本信息

### 事务的基本要素

**原子性** (Atomicity）：事务开始后所有操作，要么全部做完，要么全部不做，不可能停滞在中间环节。事务执行过程中出错，会回滚到事务开始前的状态，所有的操作就像没有发生一样。也就是说事务是一个不可分割的整体，就像化学中学过的原子，是物质构成的基本单位。
**一致性** （Consistency）：事务开始前和结束后，数据库的完整性约束没有被破坏 。比如A向B转账，不可能A扣了钱，B却没收到。
**隔离性** (Isolation) : 同一时间，只允许一个事务请求同一数据，不同的事务之间彼此没有任何干扰。
**持久性** （Durability）：事务完成后，事务对数据库的所有更新将被保存到数据库，不能回滚 。

### 事务的并发问题

**脏读** ：事务A读取了事务B更新的数据，然后B回滚操作，那么A读取到的数据是脏数据
**不可重复读** ：事务 A 多次读取同一数据，事务 B 在事务A多次读取的过程中，对数据作了更新并提交，导致事务A多次读取同一数据时，结果 不一致。--  侧重于修改
**幻读** ：系统管理员A将数据库中所有学生的成绩从具体分数改为ABCDE等级，但是系统管理员B就在这个时候插入了一条具体分数的记录，当系统管理员A改结束后发现还有一条记录没有改过来，就好像发生了幻觉一样，这就叫幻读 。-- 侧重于新增和删除 。

​    解决方案 ：解决脏读需要把数据库事务隔离级别提高到读已提交级别，解决不可重复读需要锁满足条件的行，而解决幻读要锁表 。

## 数据库事务

### mysql 事务隔离级别

1 读未提交 read-uncommitted
2 读已提交 read-committed
3 可重复读 repeatable-read
4 串行化 Serializable 
mysql 的数据执行引擎有多种，InnoDB 是支持事务的。

|       事务隔离级别        | 脏读 | 不可重复读 | 幻读 |
| :-----------------------: | :--: | :--------: | :--: |
| 读未提交 read-uncommitted |  是  |     是     |  是  |
|  读已提交 read-committed  |  否  |     是     |  是  |
| 可重复读 repeatable-read  |  否  |     否     |  是  |
|    串行化 Serializable    |  否  |     否     |  否  |

## spring事务

### spring事务管理处理方式

spring事务处理方式有2种，第一种是编程式事务，第二种是申明式事务。

编程式事务：编程式事务是侵入性事务管理，spring提供了2个类 ， 
org.springframework.transaction.support.TransactionTemplate  
和 org.springframework.transaction.PlatformTransactionManager ，
spring官方推荐使用 TransactionTemplate 。

申明式事务：声明式事务是建立在 AOP 上的，本质是对方法执行前、中、后进行拦截的，在方法之前加入或者新建一个事务，然后根据方法的执行情况选择提交事务或者回滚事务。

对比2中事务处理： 从编程方式和简洁上看 ，申明式事务要比编程式事务优雅；从控制力度上看，申明式事务是基于方法的，而编程式事务力度更细，控制到方法级别，还可以控制到代码块级别，编程式事务控制力度更细；

### spring申明式事务的5个特性

- 事务传播机制
- 事务隔离级别
- 事务只读
- 事务超时
- 事务回滚规则

####  事务传播机制

事务传播性发生在事务嵌套中，一个事务中嵌套了另一个事务，2个方法是各自作为独立的方法提交事务，或者是内存事务合并到外层事务一起提交，这个控制要使用事务传播机制，配置事务的传播机制，spring事务按照配置执行。

- **PROPAGATION_REQUIRED** 
  Spring默认的传播机制，能满足绝大部分业务需求，如果外层有事务，则当前事务加入到外层事务，一块提交，一块回滚。如果外层没有事务，新建一个事务执行 
- **PROPAGATION_REQUIRES_NEW** 
- 该事务传播机制是每次都会新开启一个事务，同时把外层事务挂起，当当前事务执行完毕，恢复上层事务的执行。如果外层没有事务，执行当前新开启的事务即可
- **PROPAGATION_SUPPORT** 
  如果外层有事务，则加入外层事务，如果外层没有事务，则直接使用非事务方式执行。完全依赖外层的事务
- **PROPAGATION_NOT_SUPPORT**
  该传播机制不支持事务，如果外层存在事务则挂起，执行完当前代码，则恢复外层事务，无论是否异常都不会回滚当前的代码
- **PROPAGATION_NEVER**
  该传播机制不支持外层事务，即如果外层有事务就抛出异常
- **PROPAGATION_MANDATORY**
  与NEVER相反，如果外层没有事务，则抛出异常
- **PROPAGATION_NESTED**
  该传播机制的特点是可以保存状态保存点，当前事务回滚到某一个点，从而避免所有的嵌套事务都回滚，即各自回滚各自的，如果子事务没有把异常吃掉，基本还是会引起全部回滚的。

```
#@Transactional 注解
package org.springframework.transaction.annotation;
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {
    @AliasFor("transactionManager")
    String value() default "";
    @AliasFor("value")
    String transactionManager() default "";
    // 事务传播机制
    Propagation propagation() default Propagation.REQUIRED;
    //  事务隔离级别
    Isolation isolation() default Isolation.DEFAULT;
    int timeout() default -1;
    boolean readOnly() default false;
    Class<? extends Throwable>[] rollbackFor() default {};
    String[] rollbackForClassName() default {};
    Class<? extends Throwable>[] noRollbackFor() default {};
    String[] noRollbackForClassName() default {};
}
```

```
# Propagation spring 事务传播机制枚举类
package org.springframework.transaction.annotation;
public enum Propagation {
	/**
	 * Support a current transaction, create a new one if none exists.
	 * 加入外层事务一起执行，如果外层不存在事务则新建一个事务
	 * <p>This is the default setting of a transaction annotation.
	 */
	REQUIRED(TransactionDefinition.PROPAGATION_REQUIRED),
	/**
	 * Support a current transaction, execute non-transactionally if none exists.
	 * 加入外层事务，如果外层不存在事务则已非事务方式执行
	 */
	SUPPORTS(TransactionDefinition.PROPAGATION_SUPPORTS),
	/**
	 * Support a current transaction, throw an exception if none exists.
	 * 加入外层事务，如果外层不存在事务则抛出异常
	 */
	MANDATORY(TransactionDefinition.PROPAGATION_MANDATORY),
	/**
	 * Create a new transaction, and suspend the current transaction if one exists.
	 * 创建新事务执行，当外层存在事务则把外层事务挂起，等内层事务执行完则恢复外层事务
	 */
	REQUIRES_NEW(TransactionDefinition.PROPAGATION_REQUIRES_NEW),
	/**
	 * Execute non-transactionally, suspend the current transaction if one exists.
	 * 总是以非事务方式执行，如果外层存在事务则挂起外层事务
	 */
	NOT_SUPPORTED(TransactionDefinition.PROPAGATION_NOT_SUPPORTED),
	/**
	 * Execute non-transactionally, throw an exception if a transaction exists.
	 * 总是以非事务方式执行，当外层存在事务则抛出异常
	 */
	NEVER(TransactionDefinition.PROPAGATION_NEVER),
	/**
	 * Execute within a nested transaction if a current transaction exists
	 * 如果外层存在事务，则嵌套在外层事务中一起执行
	 */
	NESTED(TransactionDefinition.PROPAGATION_NESTED);
	
	private final int value;
	Propagation(int value) { this.value = value; }
	public int value() { return this.value; }

}
```

#### 事务隔离级别

spring事务隔离级别使用的是数据库的事务隔离级别。

```
# Isolation spring 事务隔离级别枚举类 ，spring 事务隔离级别使用数据库的隔离级别。
package org.springframework.transaction.annotation;
public enum Isolation {
    DEFAULT(-1),
    READ_UNCOMMITTED(1),
    READ_COMMITTED(2),
    REPEATABLE_READ(4),
    SERIALIZABLE(8);
    private final int value;
    private Isolation(int value) {
        this.value = value;
    }
    public int value() {
        return this.value;
    }
}
```

#### spring事务常量值定义

spring事务中使用的传播机制和隔离级别常量值定义，在 TransactionDefinition 类中。

```
#TransactionDefinition 事务定义(传播机制和隔离级别)
package org.springframework.transaction;
public interface TransactionDefinition {
	/**
	 * Support a current transaction; create a new one if none exists.
	 */
	int PROPAGATION_REQUIRED = 0;
	/**
	 * Support a current transaction; execute non-transactionally if none exists.
	 */
	int PROPAGATION_SUPPORTS = 1;
	/**
	 * Support a current transaction; throw an exception if no current transaction
	 * exists. 
	 */
	int PROPAGATION_MANDATORY = 2;
	/**
	 * Create a new transaction, suspending the current transaction if one exists.
	 */
	int PROPAGATION_REQUIRES_NEW = 3;
	/**
	 * Do not support a current transaction; rather always execute non-transactionally.
	 */
	int PROPAGATION_NOT_SUPPORTED = 4;
	/**
	 * Do not support a current transaction; throw an exception if a current transaction
	 * exists. 
	 */
	int PROPAGATION_NEVER = 5;
	/**
	 * Execute within a nested transaction if a current transaction exists,
	 * behave like {@link #PROPAGATION_REQUIRED} else. 
	 */
	int PROPAGATION_NESTED = 6;


	/**
	 * Use the default isolation level of the underlying datastore.
	 * All other levels correspond to the JDBC isolation levels.
	 * @see java.sql.Connection
	 */
	int ISOLATION_DEFAULT = -1;

	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * can occur.
	 * <p>This level allows a row changed by one transaction to be read by another
	 * transaction before any changes in that row have been committed (a "dirty read").
	 * If any of the changes are rolled back, the second transaction will have
	 * retrieved an invalid row.
	 * @see java.sql.Connection#TRANSACTION_READ_UNCOMMITTED
	 */
	int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;
	/**
	 * Indicates that dirty reads are prevented; non-repeatable reads and
	 * phantom reads can occur.
	 * <p>This level only prohibits a transaction from reading a row
	 * with uncommitted changes in it.
	 * @see java.sql.Connection#TRANSACTION_READ_COMMITTED
	 */
	int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;
	/**
	 * Indicates that dirty reads and non-repeatable reads are prevented;
	 * phantom reads can occur.
	 * <p>This level prohibits a transaction from reading a row with uncommitted changes
	 * in it, and it also prohibits the situation where one transaction reads a row,
	 * a second transaction alters the row, and the first transaction re-reads the row,
	 * getting different values the second time (a "non-repeatable read").
	 * @see java.sql.Connection#TRANSACTION_REPEATABLE_READ
	 */
	int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;
	/**
	 * Indicates that dirty reads, non-repeatable reads and phantom reads
	 * are prevented.
	 * <p>This level includes the prohibitions in {@link #ISOLATION_REPEATABLE_READ}
	 * and further prohibits the situation where one transaction reads all rows that
	 * satisfy a {@code WHERE} condition, a second transaction inserts a row
	 * that satisfies that {@code WHERE} condition, and the first transaction
	 * re-reads for the same condition, retrieving the additional "phantom" row
	 * in the second read.
	 * @see java.sql.Connection#TRANSACTION_SERIALIZABLE
	 */
	int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;
	/**
	 * Use the default timeout of the underlying transaction system,
	 * or none if timeouts are not supported.
	 */
	int TIMEOUT_DEFAULT = -1;
	/**
	 * Return the propagation behavior.
	 */
	int getPropagationBehavior();
	/**
	 * Return the isolation level.
	 */
	int getIsolationLevel();

	/**
	 * Return the transaction timeout.
	 */
	int getTimeout();

	/**
	 * Return whether to optimize as a read-only transaction.
	 * <p>The read-only flag applies to any transaction context, whether
	 * backed by an actual resource transaction
	 * ({@link #PROPAGATION_REQUIRED}/{@link #PROPAGATION_REQUIRES_NEW}) or
	 * operating non-transactionally at the resource level
	 * ({@link #PROPAGATION_SUPPORTS}). In the latter case, the flag will
	 * only apply to managed resources within the application, such as a
	 * Hibernate {@code Session}.
	 <<	 * <p>This just serves as a hint for the actual transaction subsystem;
	 * it will <i>not necessarily</i> cause failure of write access attempts.
	 * A transaction manager which cannot interpret the read-only hint will
	 * <i>not</i> throw an exception when asked for a read-only transaction.
	 * @return {@code true} if the transaction is to be optimized as read-only
	 * @see org.springframework.transaction.support.TransactionSynchronization#beforeCommit(boolean)
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#isCurrentTransactionReadOnly()
	 */
	boolean isReadOnly();

	/**
	 * Return the name of this transaction. Can be {@code null}.
	 * <p>This will be used as the transaction name to be shown in a
	 * transaction monitor, if applicable (for example, WebLogic's).
	 * <p>In case of Spring's declarative transactions, the exposed name will be
	 * the {@code fully-qualified class name + "." + method name} (by default).
	 * @return the name of this transaction
	 * @see org.springframework.transaction.interceptor.TransactionAspectSupport
	 * @see org.springframework.transaction.support.TransactionSynchronizationManager#getCurrentTransactionName()
	 */
	String getName();

}
```

#### 配置样例

spring事务使用可以使用 xml 基于 <tx> 和 <aop> 实现。也可以使用 @Transactional 注解实现。目前使用注解的多些。

1. 事务的传播性：
   @Transactional(propagation=Propagation.REQUIRED)
2. 事务的隔离级别：
   @Transactional(isolation = Isolation.READ_UNCOMMITTED)
3. 只读：
   @Transactional(readOnly=true)
   该属性用于设置当前事务是否为只读事务，设置为true表示只读，false则表示可读写，默认值为false。
4. 事务的超时性：
   @Transactional(timeout=30)
5. 回滚：
   指定单一异常类：@Transactional(rollbackFor=RuntimeException.class)
   指定多个异常类：@Transactional(rollbackFor={RuntimeException.class, Exception.class})

### spring 声明式事务实现原理

spring 声明式事务管理的实现采用的是 AOP 技术。颗粒度是控制到方法级别。spring-tx 包中 是 spring 事务的实现。声明式事务实现大概分几个过程：对下行文中配置的属性处理 、 创建事务的过程 、 通过相关信息判断使用不同的事务管理来完成事务的处理。使用到了 IOC ，源码看 spring-tx 包，这个工程的整体分 注解包 annotation ，配置包 config ，事件包 event ，拦截器包 interceptor ， jta 包 ， support 包，和一些自定义异常类（还有平台事务管理器 PlatformTransactionManager ， 切点管理器 SavepointManager ，事务属性信息定义 TransactionDefinition ，事务状态 TransactionStatus ）。



事务管理器
PlatformTransactionManager  平台事务管理器 ；
AbstractPlatformTransactionManager 实现了 PlatformTransactionManager ；
不同的事务管理各自进行实现，比如 JpaTransactionManager 、 DataSourceTransactionManager 、 RabbitTransactionManager 等等;
TransactionDefinition 事务的定义，包含传播机制、隔离级别等；
TransactionAttributeSourceAdvisor 事务中对切面上下文属性的处理，是一个通知器；事务的实现信息存储在 TransactionAttribute  类中，它有多个实现；
TransactionProxyFactoryBean 创建事务的工厂类，继承了 AbstractSingletonProxyFactoryBean 类，使用事务拦截器 TransactionInterceptor ，



**技术点补充说明**：
1 . spring 的 Aware  接口 ：
 Aware 接口是一个根接口，里面是空的，没有任何属性和行为。可以把 Aware 理解成一个标记接口。想要是用 spring 自身的资源，则要感知到 spring 容器的存在。使用 Aware 的目的是让 bean 获得 spring 容器的服务。 Aware 常用子类 ： BeanNameAware  , BeanFactoryAware , ApplicationContextAware 等等 。
2 . dx
