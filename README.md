HighAvailabilityToolkit
======

High Availability Toolkit includes several solutions by which achieving architecture with high availability is a easy thing.
It includes "master-slaver","gossip ha","load balancer" and so on ....

###背景
互联网企业应用核心系统的高可用性保护了关键数据的完整性 并维持了应用连续运行 ，关键数据的丢失或运行的中断有时是灾难性的。目前已知的企业高可用性方案特点：
1）高可用方案与其业务逻辑紧密相连，复杂且可读性差，代码的可移植性，复用性不高
2）实现的思路多种多样
3）基于一个思路的技术实现多种多样

High Availability Toolkit提供了若干种当前业界主流方案，每种方案不掺杂业务逻辑，纯粹并简约,你要做的仅仅是打包 ，然后实现若干接口，当然强烈建议你先看看demo；
或许你的业务需求特别复杂，不是实现几个接口就可以实现的，High Availability Toolkit 会给你提供若干个清晰的思路，依靠这些思路，事半功倍。

###关于项目
#####运行环境
		jdk1.7

#####编译及打包
		clean
		compile
		package

#####Demo
master-slaver :  <br/>
test/com.wp.ha.zk.DemoRunning<br/>
test/com.wp.ha.zk.DemoRunningListener
<br/>
gossip cluster : <br/>
java -jar gossip-java.jar ,then gossip-java will load gossip.conf and run.  <br/>
<br/>
<br/>
![](https://github.com/yfwangpeng/HighAvailabilityToolkit/blob/master/img/gossip_protocol.jpg)
<br/>
<br/>
load balancer : <br/>
LoadbalanceClientDemo<br/>
LoadbalanceServerDemo<br/>


###roadmap
Commits on Oct 12, 2014<br/>
master-slaver<br/>
Commits on Oct 20, 2014<br/>
high availability based on gossip protocol<br/>
gossip-java.jar come from http://code.google.com/p/java-gossip<br/>
Commits on Nov 13, 2014<br/>
load balancer was developed to enable distributing the load among one or more back-end servers,
a fail-over server will stop passing requests from client to it until it recovers. The client is to specify RoundRobinLoadBalance implementations to use
in LoadbalanceClientDemo.java, of course you can specify other implementation .<br/>
....<br/>
....<br/>
....<br/>
