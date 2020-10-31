# 一个异常通知的spring-boot-start框架 prometheus-spring-boot-starter

## 前言

前一段时间很忙，也没时间更新，最近趁着双旦刚过的闲暇时间把这个异常通知框架的team分支进行一次大更新。


## 2020-01-03更新（大更新）

1. 版本号正式升级为**0.5.1-team**
2. 新增：**异步通知**
3. 新增：异常通知文本结构自定义
4. 新增：异常通知的**环境类型**
5. 新增：钉钉的**markdown文本支持**
6. 新增：**钉钉的加签验证**
7. 改进：自定义钉钉通知流程
8. 改进：config自动化配置
9. 改进：钉钉的调用所需的HTTPclient可自行配置，默认配置改为spring中resttemplate，原来的SimpleHttpclient已被废弃，这意味着依赖中需要spring-boot-starter-web
10. 改进：对于异常信息中caused by进行了处理
11. 修复bug


## 2019-06-24更新

1. 改进：web处理增加一个拦截器，专门清除为异常通知保存的请求体信息
2. 修复bug：修复了初次出现异常产生的并发导致重复发送的问题
3. 当前版本号变为**0.3.5-team**


## 前言

对于工程的开发，必然会伴随着各种bug，工程量越大，出现bug的频率也会越高。一般对于代码量较小的工程来说，一个人可能就足够去做开发与维护；但是对于代码量较大的工程往往是需要一个小团队协作开发。当工程基本完成，开始部署测试环境或者生产环境时，这些环境并不能像开发环境一样能快速的调试与维护，线上的工程一旦出现异常时，开发团队就需要主动感知异常并协调处理，当然人不能一天24小时去盯着线上工程，所以就需要一种机制来自动化的对异常进行通知，并精确到谁负责的那块代码。这样会极大地方便后续的运维。因此，本项目的团队版上线

## 系统需求

![jdk版本](https://img.shields.io/badge/java-1.8%2B-red.svg?style=for-the-badge&logo=appveyor)
![maven版本](https://img.shields.io/badge/maven-3.2.5%2B-red.svg?style=for-the-badge&logo=appveyor)
![spring boot](https://img.shields.io/badge/spring%20boot-2.0.0.RELEASE%2B-red.svg?style=for-the-badge&logo=appveyor)

## 当前版本

![目前工程版本](https://img.shields.io/badge/version-0.5.1--team-green.svg?style=for-the-badge&logo=appveyor)


## 最快上手

1. 将此工程通过``mvn clean install``打包到本地仓库中。
2. 在你的工程中的``pom.xml``中做如下依赖
```
		<dependency>
			<groupId>com.kuding</groupId>
			<artifactId>prometheus-spring-boot-starter</artifactId>
			<version>0.5.1-team</version>
		</dependency>
```
3. 在``application.properties``或者``application.yml``中做如下的配置：（至于以上的配置说明后面的章节会讲到）
```
exceptionnotice:
  open-notice: true
  default-notice: user1
  project-enviroment: develop
  included-trace-package:你的工程目录
  
  dingding:    
    user1:
      phone-num: 手机号（数组）
      web-hook: 钉钉的webhook
```
4. 至于钉钉的配置请移步：[钉钉机器人](https://open-doc.dingtalk.com/microapp/serverapi2/krgddi "自定义机器人")，这里需要特别说明一下，钉钉在2019年10月份（大概）对于钉钉机器人进行了一次改进,这次改进主要是安全设置变为必选项，原来已经配置过的钉钉机器人且没有配置安全设置的不受影响
![钉钉改进](/src/main/resources/dingdingupgrade.png)

5. 以上配置好以后就可以写demo测试啦，首先创建第一个bean：
```
@Component
@ExceptionListener // 异常通知的监控来自这个注解
public class NoticeComponents {

	public void someMethod(String name) {
		System.out.println("这是一个参数：" + name);
		throw new NullPointerException("第一个异常");
	}

	public void anotherMethod(String name, int age) {
		System.out.println("这又是一个参数" + age);
		throw new IllegalArgumentException(name + ":" + age);
	}
}

```
6. 以上都建立好了以后，就可以写单元测试了:
```
@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Autowired
	private NoticeComponents noticeComponents;

	@Test
	public void contextLoads() {
		noticeComponents.someMethod("hello");
	}
}
```
当运行单元测试后，假如钉钉配置没有问题的话，你的钉钉中就会出现如下类似的消息：
![效果](/src/main/resources/simpledingding.png)
假如在你配置的钉钉中出现类似这个信息的化，恭喜你，你成功的产生了一个异常通知。

综上，一个最简单的例子就完成了。

## 咋做的

本框架遵循spring boot starter的自动化配置规范而开发的自动化异常通知框架，在原有的单人版基础上进行了多处改进并升级成团队版，整体业务流程如下：
![流程](/src/main/resources/liucheng2.png)

### 配置

本框架配置主要分为4部分：
1. 全局配置
2. 背锅用户配置
3. 策略配置
4. 外援配置

#### 全局配置

- 下面是所有的全局配置（yml版）：
```
exceptionnotice:
  open-notice: true
  project-name: project
  default-notice: user1
  project-enviroment: develop/test/preview/release/rollback
  dingding-text-type: text/markdown
  email-text-type: text
  included-trace-package: com.havefun
  listen-type: common/web-mvc
  include-header-name:
  - headparam
  enable-async-notice: true
  enable-redis-storage: true
  redis-key: exceptionnotice-demo
  exclude-exceptions:
  - java.lang.IllegalArgumentException
```

- 具体说明如下：

|名称|参数类型|说明|必要配置|
|:-:|:-:|:-:|:-:|
|open-notice|boolean|用于开启异常通知（必填）|是|
|project-name|string|一般忽略，以spring.application.name替代|否|
|default-notice|string|默认的背锅侠，用于@ExceptionListener缺省value值|否|
|project-enviroment|enum|表示此工程环境，用于标注是哪个环境中的工程出异常|是|
|dingding-text-type|enum|钉钉通知的文本类型（默认为text）|否|
|email-text-type|enum|邮件通知的文本类型（只有text，没做扩展）|否|
|included-trace-package|string|异常追踪的包路径，一般情况下，此配置项就是配置你工程的包路径就可以了|是|
|listen-type|enum|监听类型，有两种：**common/web-mvc**，默认为common|是|
|include-header-name|list|（**web-mvc限定**）异常通知中需要包含的header信息(不写全部返回)|否|
|enable-async-notice|boolean|是否开启**异步**通知（默认否）|否|
|enable-redis-storage|boolean|是否开启redis存储（默认否）|否|
|redis-key|string|(**开启redis限定**)redis存储的键值|否|
|exclude-exceptions|list|排除异常，表示这些异常不需要进行异常通知|否|

- ``project-enviroment``主要用于区分线上异常通知的环境问题，对于日常开发到测试部署到正式部署，配置信息不管通过传统的工程内properties控制还是通过微服务配置中心统一配置，都需要区别工程环境的问题，所以为了方便对比，加入了此配置项

- 以上通知中**最重要**的当属``exceptionnotice.listen-type``,此配置表示工程的监听方式，目前有两种监听方式：**普通监听（common）** ；**mvc监听（web-mvc）** 。这两种监听方式各有千秋，普通监听方式主要运用aop的方式对有注解的方法或类进行监听，可以加在任何类与方法上。**mvc监听只能对controller层进行监听，对其它层无效**，不过异常通知的信息更丰富，不仅仅包括了普通监听的所有信息（不包含参数），还包含了请求中的路径信息（path）、参数信息（param）、请求中的请求体信息（body）和请求体中的头信息（header）。例如：
```
@RestController
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	private DingdingNoticeService dingdingNoticeService;

	@Autowired
	private CustomService customService;

	@PostMapping("/dingding1/{pathParam}")
	@ExceptionListener("user1")
	public String dingding1(@PathVariable @ApiParam(value = "来个路径参数", required = true) String pathParam,
			@RequestParam @ApiParam(value = "来个参数", required = true) String param,
			@RequestHeader @ApiParam(value = "来个请求头", required = true) String headParam,
			@RequestBody @ApiParam(value = "来个请求体", required = true) String body) {
		dingdingNoticeService.dinding1();
		return "Keep on going never give up.";
	}
	...more code
```
具体效果如下：

![请求异常通知](/src/main/resources/mvcnormal.png)

- 实际上钉钉机器人提供了很多其他的消息类型，这里适配了**钉钉的markdown**文本输出，只需要配置``exceptionnotice.dingding-text-type=markdown``即可，最终你可以得到以下面类似的效果：

![请求异常通知markdown](/src/main/resources/mvcmarkdown.png)

- 项目中的异常一般分类两大类：第一类为未捕获异常，第二类为业务异常。业务异常一般由用户自己定义的异常，在javaweb项目中，假如用户的请求不满足返回结果的条件，一般是需要主动抛出自定义异常的，所以这类异常并不需要进行通知。排除不需要异常通知的配置如下：
```
 exceptionnotice.exclude-exceptions=java.lang.IllegalArgumentException,com.yourpackage.YourLogicException
```

- 为了方便进行扩展开发，本框架还支持异常信息的持久化，目前只支持进行redis的持久化，开启redis持久化需要进行如下配置
```
exceptionnotice.enable-redis-storage=true
exceptionnotice.redis-key=你自己的redis键
```
**这里开启redis存储需要依赖spring-boot-starter-data-redis，需要用户自行配置**，不过目前redis的存储不建议使用，后续我会完善异常持久化的流程

- 上一版本的异常通知由于是同步的，由于各种各样的原因，在异常通知失败时，框架本身可能也会产生一些异常，所以增加了异步通知，主要配置为``exceptionnotice.enable-async-notice=true``，开启异常配置需要配置一个自有的线程池，具体配置如下：
```
exceptionnotice:
  async:
    core-pool-size: 1
    max-pool-size: 100
    daemon: false
    queue-capacity: 50
    thread-name-prefix: notice-
```
以上配置实际上对应着spring中的``ThreadPoolTaskExecutor``一部分配置，配置的东西也比较简单，要是有具体的其他特殊的线程配置请留言提建议

#### 背锅用户配置

- 背锅用户的配置分为两种类型：
  1. 背锅钉钉通知用户
  2. 背锅邮件通知用户

以上两种类型都通过map类型进行配置：
```
/**
	 * 发送钉钉异常通知给谁
	 */
	Map<String, DingDingExceptionNoticeProperty> dingding;

	/**
	 * 发送邮件异常通知给谁
	 */
	Map<String, EmailExceptionNoticeProperty> email;
```
**其中map的key表示的是谁需要来背锅，也是注解``@ExceptionListener``所需要填写的参数的值**

- 背锅钉钉通知的钉钉配置：
```
exceptionnotice:
  dingding:    
    user1:
      phone-num: 手机号（数组）
      web-hook: 钉钉的web钩子
      enable-signature-check: 是否开启签名验证（true/false）
      sign-secret: 签名秘钥
    user2：
      ...........
```
开启签名验证时，需要在钉钉机器人的安全设置中选中*加签*设置：
![钉钉加签](/src/main/resources/dingdingsign.png)

- 背锅邮件通知同样也延续了原来的邮件配置，同样依赖``spring-boot-starter-mail``及其配置
```
spring:
  mail:
    host: smtp.xxx.com
    port: 25
    username: 开启smtp权限的邮箱用户名
    password: 密码
exceptionnotice:
  email:
    user3:
      from: 发件人
      to: 给谁发
      cc: 抄送给谁
      bcc: 秘密抄送给谁
```

#### 策略配置

一般而言，一个方法出现异常信息，意味着每当同样的方式进行调用时都会抛出相同的异常方法，放任不管的话，钉钉异常通知与邮件异常通知会重复的收到同一个异常，所以为了限制发送频率，默认情况下，某个方法出现的异常需要通知，那么这条通知**每天只会出现一次**。当然这样也可能会出现问题，假如邮件或钉钉信息没有收到，很可能会漏掉此通知，所以这里创造了一个通知的策略。

- 通知策略对应的配置类为``ExceptionNoticeFrequencyStrategy``，开启策略需要在``application.properties``中加入如下配置
```
exceptionnotice.strategy.enabled=true
```
- 目前一共有两种通知策略
    - 时间策略
    - 出现频次策略

对应的配置为：
```
exceptionnotice.strategy.frequency-type=timeout/showcount
```
- 时间策略对应的配置项为``exceptionnotice.strategy.notice-time-interval``，类型为duration，表示的是自上次通知时间起，超过了此配置的时间后，便会再次通知。
- 频次策略对应的配置项为``exceptionnotice.strategy.notice-show-count``，表示的是自上次通知起，出现次数超过了此配置测次数后，便会再次通知

基本上以上策略足够使用，假如说还有更好的配置策略可以连系我

#### 外援配置

目前需要外援配置的信息有以下几个，其实上面也提到了

1. 开启redis存储的话需要在``pom.xml``中加入如下依赖
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>
```
加入依赖后需要开始配置redis
```
spring:
  redis:
    host: 127.0.0.1
    port: 6379
    database: 0
    password: 密码
```

2. 有邮件通知的话需要在``pom.xml``中加入如下依赖
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-mail</artifactId>
		</dependency>
```
加入依赖后开始配置邮件信息
```
spring:
  mail:
    host: smtp.xxx.com
    port: 25
    username: 开启smtp权限的邮箱用户名
    password: 密码
```

3. 开启web-mvc模式的话，不说也知道，一定会引入如下依赖：
```
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
```


### 注解

- 上面讲的配置实际上是为此注解服务的，框架内唯一的注解``@ExceptionListener``，需要注意的是，凡是你需要异常通知的类或方法上必须加此注解。
- 根据配置类型``exceptionnotice.listen-type``的不同配置注解的位置也是不一样的
    - ``exceptionnotice.listen-type=common``时``@ExceptionListener``可以加到**任意类上，任意方法上**
    - ``exceptionnotice.listen-type=web-mvc``时，``@ExceptionListener``只能加在Controller层即带有``@Controller``或``@RestController``的类或者方法上，方法上也需要有对应的``@RequestMapping``相关的注解


### 自定义Config

- 目前在上面所有的配置的基础上，本框架还加入了更多的扩展配置，主要包含以下几个模块：
	1. 自定义的消息通知组件
	2. 自定义的异常通知文本结构
	3. 自定义的http组件

- 自定义消息组件主要由接口``INoticeSendComponent``进行消息发送:
```
public interface INoticeSendComponent {
	public void send(String blamedFor, ExceptionNotice exceptionNotice);
	public Collection<String> getAllBuddies();
}
```
其中主要的方法为``public void send(String blamedFor, ExceptionNotice exceptionNotice)``表示为我的异常通知（ExceptionNotice）需要发给谁（blameFor）。``public Collection<String> getAllBuddies()``表示那些buddy需要用这个消息通知组件进行消息发送：

```
@Component
public class MySendExceptionComponent implements INoticeSendComponent {
	
	private List<String> allBoddies = Arrays.asList("Tom", "Jerry");
	
	private final Log logger = LogFactory.getLog(getClass());
	
	@Override
	public void send(String blamedFor, ExceptionNotice exceptionNotice) {
		logger.error(blamedFor + "-->" + exceptionNotice.getExceptionMessage());
	}
	
	@Override
	public Collection<String> getAllBuddies() {
		return allBoddies;
	}
}
```
配置好以后需要加入到现有的配置体系中，所以需要增加一个spring的config配置并实现``ExceptionSendComponentConfigure``接口：
```
@Configuration
public class CustomSendComponentConfig implements ExceptionSendComponentConfigure {

	@Autowired
	private MySendExceptionComponent mySendExceptionComponent;

	@Override
	public void addSendComponent(ExceptionHandler exceptionHandler) {
		exceptionHandler.registerNoticeSendComponent(mySendExceptionComponent);
	}

}
```
这样，自定义的消息通知系统就可以通过``@ExceptionListener``注解来使用了
```
	@PutMapping("/custom")
	@ExceptionListener("Tom")
	public String custom() {
		customService.custom();
		return "Whatever is worth doing is worth doing well.";
	}
//2020-01-03 20:30:46.480 ERROR 1576 --- [ometheus-task-1] c.h.components.MySendExceptionComponent  : Tom-->[java.lang.NullPointerException:自定义通知第一个]
```

- 自定义异常通知文本接口主要是用户可以自行编辑所需要的文本结构进行通知，控制输出异常通知的文本是由``ExceptionNoticeResolverFactory``进行管理的，通过管理``ExceptionNoticeResolver``来输出不同的文本。所以需要自己的异常消息配置需要实现实现``ExceptionNoticeResolver``接口：
```
@Component
public class MyExceptionNoticeResolver implements ExceptionNoticeResolver {

	@Override
	public String resolve(ExceptionNotice exceptionNotice) {
		StringBuilder builder = new StringBuilder();
		builder.append("人非圣贤，孰能无错？").append("\n");
		exceptionNotice.getExceptionMessage().forEach(x -> builder.append(x).append("\n"));
		return builder.toString();
	}
}
```
然后将此bean交由``ExceptionNoticeResolverFactory``进行管理，可以通过spring的config配置并实现``ExceptionNoticeResolverConfigure``接口
```
@Configuration
public class MyExceptionTextResolverConfig implements ExceptionNoticeResolverConfigure {

	@Autowired
	private MyExceptionNoticeResolver myExceptionNoticeResolver;

	@Override
	public void addResolver(ExceptionNoticeResolverFactory factory) {
		factory.addNoticeResolver("dingding", myExceptionNoticeResolver);
	}
}
```
这里需要说明一下，``ExceptionNoticeResolverFactory``中的方法``public void addNoticeResolver(String resolveKey, ExceptionNoticeResolver resolver)``的``resolveKey``表示的对``ExceptionNoticeResolver``对象的唯一标识。框架提供的dingding通知与email通知有其默认的解析标识，分别为``dingding``与``email``，所以假如你有特殊的钉钉通知或email通知需要，可以通过上述的方式进行自定义。
![钉钉加签](/src/main/resources/customresolver.png)


### 有些需要注意的地方

#### 1、对于配置exceptionnotice.listen-type=web-mvc，请注意是“web-mvc”，不是“web_mvc”，也不是“WEB_MVC”我发现很多网友这块配置老出问题

#### 2、0.5.1-team这个版本默认需要依赖spring-boot-starter-web，所以在工程中配置此框架必须要引入spring-boot-starter-web，我这块还没处理好，所以对于非web项目目前请自行改造


[![Fork me on Gitee](https://gitee.com/ITEater/prometheus-spring-boot-starter/widgets/widget_2.svg)](https://gitee.com/ITEater/prometheus-spring-boot-starter)
