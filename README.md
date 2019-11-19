# apollo-cloud
Spring Cloud 一系列组件（Config、Bus、Eureka、Gateway、Ribbon、Feign、Hystrix）

###项目介绍
- apollo-common公共配置类
- apollo-config 配置中心
- apollo-eureka 注册中心
- apollo-feign  feign接口暴露
- apollo-server 服务提供者
- apollo-gateway 网管
注册中心和配置中心可以采用集群方式部署

1、eureka server配置如下
```
eureka:
  instance:
    hostname: localhost
  client:
    service-url:
      defaultZone: http://${name}:${password}@${hostname}:${server.port}/eureka/
    fetch-registry: true
    # 是否把自己作为服务注册到其他服务注册中心
    register-with-eureka: false
```
fetch-registry同步其他节点的数据

2、配置中心高可用方式，客户端链接需要采用一下方式

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
