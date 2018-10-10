# 客户端集成

[大众点评CAT Github地址](https://github.com/dianping/cat#quick-start)：https://github.com/dianping/cat

本项目访问：http://localhost:8079/cat
服务端：http://9.186.54.96:9001/cat/r
**一般步骤**

- 引包
- 加filter
- 埋点

## 一、客户端机器配置

### 1、配置数据文件路径

确保所有CAT客户端以及服务器对于/data目录具有读写权限。

> mkdir -p /data/appdatas/cat
>
> mkdir -p /data/applogs/cat
>
> sudo chmod 777 /data/appdatas/cat
>
> sudo chmod 777 /data/applogs/cat

### 2、创建client.xml

配置所有服务端的配置文件，文件路径/data/appdatas/cat/client.xml。

```xml
<config mode="client">
    <servers>
        <!-- ip为cat服务器的ip，端口默认 -->
        <server ip="9.186.54.96" port="2280"/>
    </servers>
</config>
```



## 二、spring boot接入CAT

### 1、配置domain

在资源文件中新建`app.properties`文件，在resources资源文件META-INF下，注意是`src/main/resources/META-INF/`文件夹， 而不是webapps下的那个META-INF,添加app.properties，加上domain配置，如：`app.name=cat22`

> app.name=cat22中的cat22可自定义，访问时可直接搜索，下图中的cat是服务器自带的domain
>
> ![image-20180920173632667](https://ws3.sinaimg.cn/large/006tNbRwly1fvg53rvqi7j317q07q0um.jpg)

### 2、引入依赖

`gradle`

```groovy
repositories {
	// 引入 dianping cat 
	maven { url 'http://unidal.org/nexus/content/repositories/releases/'}
}
dependencies{
	compile 'com.dianping.cat:cat-client:2.0.0'
}
```

`maven`

```xml
<repositories>
    <repository>
       <id>central</id>
       <name>Maven2 Central Repository</name>
       <layout>default</layout>
       <url>http://repo1.maven.org/maven2</url>
    </repository>
    <repository>
       <id>unidal.releases</id>
       <url>http://unidal.org/nexus/content/repositories/releases/</url>
    </repository>
 </repositories>
<dependency>
    <groupId>com.dianping.cat</groupId>
    <artifactId>cat-client</artifactId>
    <version>2.0.0</version>
</dependency>
```

### 3、加入配置信息

```java
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dianping.cat.servlet.CatFilter;

@Configuration
public class CatFilterConfigure {

	@Bean
	public FilterRegistrationBean catFilter() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		CatFilter filter = new CatFilter();
		registration.setFilter(filter);
		registration.addUrlPatterns("/*");
		registration.setName("cat-filter");
		registration.setOrder(1);
		return registration;
	}
}
```

### 4、埋点操作

埋点不支持中文

```java
String pageName = "pageName1";
String serverIp = "127.0.0.2";
double amount = 20;
Transaction t = Cat.newTransaction("URL", pageName);
try {
    // 记录一个事件
    Cat.logEvent("URL.Server12", serverIp, Event.SUCCESS, "ip=" + serverIp + "&name=1");
    // 记录一个业务指标，记录次数
    Cat.logMetricForCount("PayCount");
    // 记录一个业务指标，记录支付金额
    Cat.logMetricForSum("PayAmount", amount);
    // TODO 你的业务代码
    t.setStatus(Transaction.SUCCESS);
    return allUserSub;
} catch (Exception e) {
    t.setStatus(e);
} finally {
    t.complete();
}
	
```

