# apollo-cloud
Spring Cloud 一系列组件（Config、Bus、Eureka、Gateway、Ribbon、Feign、Hystrix）

注册中心和配置中心可以采用集群方式部署
1、配置中心高可用方式，客户端链接需要采用一下方式

```
spring: 
  cloud:
    config:
      discovery:
        service-id: apollo-config #服务名
        enabled: true #从配置中心读取文件
```


读取配置中心文件不再写ip的方式，而是服务名，这时如果配置多份，通过负责均衡，从而高可用

依次启动eureka-server、config-server、config-client
