# Nacos 配置文件

##### redis.properties

```yaml
spring:
  data:
    redis:
      database: 0
      host: localhost
      port: 6379
```

##### client.properties

```yaml
spring:
  cloud:
    openfeign:
      client:
        config:
          default:
            logger-level: BASIC
            connect-timeout: 5000
            read-timeout: 10000
feign:
  sentinel:
    enabled: true
```

##### seata.properties

```properties
seata.enabled=true
seata.application-id=${spring.application.name}
seata.tx-service-group=default_tx_group
seata.service.vgroup-mapping.default_tx_group=default
seata.service.grouplist.default=localhost:8091
```

