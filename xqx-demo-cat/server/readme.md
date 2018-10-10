# 生产环境服务端部署

[大众点评CAT Github地址](https://github.com/dianping/cat#quick-start)：https://github.com/dianping/cat

cat服务端war包下载

http://unidal.org/nexus/service/local/repositories/releases/content/com/dianping/cat/cat-home/2.0.0/cat-home-2.0.0.war

## 一、CAT安装环境

- Linux 2.6以及之上（2.6内核才可以支持epoll），线上服务端部署请使用Linux环境，Mac以及Windows环境可以作为开发环境，美团点评内部CentOS 6.5
- Java 6，7，8，服务端推荐是用jdk7的版本，客户端jdk6、7、8都支持
- Maven 3.3.3
- MySQL 5.6，5.7，更高版本MySQL都不建议使用，不清楚兼容性
- J2EE容器建议使用tomcat，建议版本7.0.70，高版本tomcat默认了get字符串限制，需要修改一些配置才可以生效，不然提交配置可能失败。
- Hadoop环境可选，一般建议规模较小的公司直接使用磁盘模式，可以申请CAT服务端，500GB磁盘或者更大磁盘，这个磁盘挂载在/data/目录上

## 二、CAT安装步骤

一般步骤：

- 下载war，放入tomcat7
- 配置文件，文件夹，读写权限
- 启动tomcat

### 1、配置生产环境数据库

自选或新建数据库，然后执行数据库脚本，在资源文件 cat-init.sql。

### 2、准备N台cat服务器

比如3台，ip为10.1.1.1，10.1.1.2，10.1.1.3。

### 3、在所有cat服务器上安装tomcat 7

启动端口默认设定为8080。

需要每台CAT集群10.1.1.1，10.1.1.2，10.1.1.3都进行部署

建议使用cms gc策略

建议cat的使用堆大小至少10G以上，开发环境启动2G堆启动即可

```
CATALINA_OPTS="$CATALINA_OPTS -server -Djava.awt.headless=true -Xms25G -Xmx25G -XX:PermSize=256m -XX:MaxPermSize=256m -XX:NewSize=10144m -XX:MaxNewSize=10144m -XX:SurvivorRatio=10 -XX:+UseParNewGC -XX:ParallelGCThreads=4 -XX:MaxTenuringThreshold=13 -XX:+UseConcMarkSweepGC -XX:+DisableExplicitGC -XX:+UseCMSInitiatingOccupancyOnly -XX:+ScavengeBeforeFullGC -XX:+UseCMSCompactAtFullCollection -XX:+CMSParallelRemarkEnabled -XX:CMSFullGCsBeforeCompaction=9 -XX:CMSInitiatingOccupancyFraction=60 -XX:+CMSClassUnloadingEnabled -XX:SoftRefLRUPolicyMSPerMB=0 -XX:-ReduceInitialCardMarks -XX:+CMSPermGenSweepingEnabled -XX:CMSInitiatingPermOccupancyFraction=70 -XX:+ExplicitGCInvokesConcurrent -Djava.nio.channels.spi.SelectorProvider=sun.nio.ch.EPollSelectorProvider -Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager -Djava.util.logging.config.file="%CATALINA_HOME%\conf\logging.properties" -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCApplicationConcurrentTime -XX:+PrintHeapAtGC -Xloggc:/data/applogs/heap_trace.txt -XX:-HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/applogs/HeapDumpOnOutOfMemoryError -Djava.util.Arrays.useLegacyMergeSort=true"
```

修改中文乱码 tomcat conf 目录下 server.xml

```xml
<Connector port="8080" protocol="HTTP/1.1"
           URIEncoding="utf-8"    <!--增加  URIEncoding="utf-8"-->
           connectionTimeout="20000"
           redirectPort="8443" />  
                            
```

### 4、配置数据文件路径

确保所有CAT客户端以及服务器对于/data目录具有读写权限。

> mkdir -p /data/appdatas/cat
>
> mkdir -p /data/applogs/cat
>
> sudo chmod 777 /data/appdatas/cat
>
> sudo chmod 777 /data/applogs/cat

### 5、创建client.xml

配置所有服务端的配置文件，文件路径/data/appdatas/cat/client.xml。

```xml
	<config mode="client">
	    	<servers>
	                <server ip="10.1.1.1" port="2280" http-port="8080"/>
	                <server ip="10.1.1.2" port="2280" http-port="8080"/>
	                <server ip="10.1.1.3" port="2280" http-port="8080"/>
	    	</servers>
	</config>
```

### 6、创建datasources.xml

配置服务端的数据库配置，文件路径/data/appdatas/cat/datasources.xml,需要替换对应的线上配置。

```xml
<data-sources>
	<data-source id="cat">
		<maximum-pool-size>3</maximum-pool-size>
		<connection-timeout>1s</connection-timeout>
		<idle-timeout>10m</idle-timeout>
		<statement-cache-size>1000</statement-cache-size>
		<properties>
			<driver>com.mysql.jdbc.Driver</driver>
			<url><![CDATA[{jdbc.url}]]></url>
			<user>{jdbc.user}</user>
			<password>{jdbc.password}</password>
			<connectionProperties><![CDATA[useUnicode=true&autoReconnect=true]]></connectionProperties>
		</properties>
	</data-source>
	<data-source id="app">
		<maximum-pool-size>3</maximum-pool-size>
		<connection-timeout>1s</connection-timeout>
		<idle-timeout>10m</idle-timeout>
		<statement-cache-size>1000</statement-cache-size>
		<properties>
			<driver>com.mysql.jdbc.Driver</driver>
			<url><![CDATA[{jdbc.url}]]></url>
			<user>{jdbc.user}</user>
			<password>{jdbc.password}</password>
			<connectionProperties><![CDATA[useUnicode=true&autoReconnect=true]]></connectionProperties>
		</properties>
	</data-source>
</data-sources>
```



### 7、创建server.xml

文件路径/data/appdatas/cat/server.xml。

```xml
<!-- 生产环境配置 -->
<!-- 注: -->
<!-- 1. 设置local-mode="false"，以激活remote mode. -->
<!-- 2. If machine is job-machine, set job-machine true, you just need config only one machine. Job is offline for report aggreation, statistics report.-->
<!-- 3. If machine is alert-machine, set alert-machine true, you just need config only one machine. -->
<!-- 4. Cat可以不使用hdfs，此时应设置hdfs-machine="false"。如果你使用hdfs, 你可以配置<hdfs/>用于保存log view信息.  -->
<!-- 5. 如果你不使用hdfs存储，那么logview将会存储到磁盘上。你可以配置最大存储时间local-logivew-storage-time="7"，单位'天'. -->
<!-- 6. Please set hadoop environment accordingly. -->
<!-- 7. Please set ldap info for login the system. -->
<!-- 8. 如果你有多台cat服务器，请配置<remote-servers> -->
<config local-mode="false" hdfs-machine="false" job-machine="false" alert-machine="false">
	<storage  local-base-dir="/data/appdatas/cat/bucket/" max-hdfs-storage-time="15" local-report-storage-time="7" local-logivew-storage-time="7">
		<hdfs id="logview" max-size="128M" server-uri="hdfs://10.1.77.86/user/cat" base-dir="logview"/>
		<hdfs id="dump" max-size="128M" server-uri="hdfs://10.1.77.86/user/cat" base-dir="dump"/>
		<hdfs id="remote" max-size="128M" server-uri="hdfs://10.1.77.86/user/cat" base-dir="remote"/>
	</storage>
	<console default-domain="Cat" show-cat-domain="true">
		<remote-servers>10.1.1.1:8080,10.1.1.2:8080,10.1.1.3:8080</remote-servers>		
	</console>
	<ldap ldapUrl="ldap://192.168.50.11:389/DC=dianpingoa,DC=com"/>
</config>

```

```xml
<!-- 我测试时使用的配置如下 -->
<config local-mode="false" hdfs-machine="false" job-machine="true" alert-machine="false">
        <storage  local-base-dir="/data/appdatas/cat/bucket/" max-hdfs-storage-time="15" local-report-storage-time="7" local-logivew-storage-time="7">
        </storage>
        <console default-domain="Cat" show-cat-domain="true">
                <remote-servers>9.186.54.96:9001</remote-servers>
        </console>

</config>
```



### 9、下载CAT war包

1. 直接下载war包

http://unidal.org/nexus/service/local/repositories/releases/content/com/dianping/cat/cat-home/2.0.0/cat-home-2.0.0.war

下载wa包并改名为cat.war，放入tomcat7的wabapp中，

1. [下载源码](https://github.com/dianping/cat)，用`maven clean pakcage -DsikpTests`打包，war包在cat/cat-home/target，将war包并、改名为cat.war，放入tomcat7的wabapp中，

### 10、启动Tomcat

启动一台服务端10.1.1.1，修改服务端路由文件，url地址 http://10.1.1.1:8080/cat/s/config?op=routerConfigUpdate，若单机测试则不需配置

​      需要用户名密码登陆，如果配置ldap即可直接登陆，或者用默认账号catadmin/catadmin登陆。

​      可以将10.1.1.1 部署为提供内部访问，并设置job-machine=true，alert-machine=true，让这台机器进行后续job以及告警处理，这些都可能影响到consumer性能。

​      将10.1.1.2,10.1.1.3 处理全部监控请求，如果后续需要扩容，可以直接添加default-server的节点。

```xml
<?xml version="1.0" encoding="utf-8"?>
<router-config backup-server="10.1.1.1" backup-server-port="2280">
   <default-server id="10.1.1.2" port="2280" enable="true"/>
   <default-server id="10.1.1.3" port="2280" enable="true"/>
</router-config>
```

### 11、重启保证数据不丢
请在tomcat重启之前调用当前tomcat的存储数据的链接 http://${ip}:8080/cat/r/home?op=checkpoint，重启之后数据会恢复。【注意重启时间在每小时的整点10-55分钟之间】
线上部署时候，建议把此链接调用存放于tomcat的stop脚本中，这样不需要每次手工调用

