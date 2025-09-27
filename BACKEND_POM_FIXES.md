# 后端POM文件调整说明

## 问题分析

1. **Spring Boot版本过高**：原版本3.5.6与Spring Cloud Alibaba不兼容
2. **Spring Cloud版本不匹配**：需要与Spring Boot 2.x版本匹配
3. **模块命名不一致**：UserService应该改为user-service
4. **依赖版本冲突**：部分依赖版本不兼容

## 解决方案

### 1. 父工程pom.xml调整

#### Spring Boot版本降级
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.7.18</version>  <!-- 从3.5.6降级 -->
    <relativePath/>
</parent>
```

#### Spring Cloud版本调整
```xml
<properties>
    <!-- Spring Cloud 版本 -->
    <spring-cloud.version>2021.0.8</spring-cloud.version>  <!-- 从2023.0.0调整 -->
    <!-- Spring Cloud Alibaba 版本 -->
    <spring-cloud-alibaba.version>2021.0.5.0</spring-cloud-alibaba.version>  <!-- 从2022.0.0.0调整 -->
</properties>
```

#### 依赖版本统一
```xml
<properties>
    <!-- Redis 版本 -->
    <jedis.version>4.4.3</jedis.version>  <!-- 从4.3.2调整 -->
    <!-- FastJSON 版本 -->
    <fastjson.version>2.0.40</fastjson.version>  <!-- 从2.0.4调整 -->
</properties>
```

#### 模块名称统一
```xml
<modules>
    <module>gateway-service</module>
    <module>auth-service</module>
    <module>user-service</module>  <!-- 从UserService调整 -->
    <module>post-service</module>
    <module>event-service</module>
    <module>chat-service</module>
    <module>ai-audit-service</module>
    <module>message-service</module>
    <module>recommend-service</module>
</modules>
```

### 2. 各服务pom.xml调整

#### 认证服务 (auth-service)
- 修复Spring Security OAuth2依赖
- 移除不兼容的authorization-server依赖
- 添加oauth2-client和oauth2-jose依赖

```xml
<!-- Spring Security OAuth2 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-client</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-oauth2-jose</artifactId>
</dependency>
```

#### AI审核服务 (ai-audit-service)
- 移除RabbitMQ的硬编码版本号
- 使用父工程管理的版本

```xml
<!-- RabbitMQ -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
    <!-- 移除version标签，使用父工程管理 -->
</dependency>
```

### 3. 版本兼容性矩阵

| 组件 | 原版本 | 调整后版本 | 说明 |
|------|--------|------------|------|
| Spring Boot | 3.5.6 | 2.7.18 | 与Spring Cloud Alibaba兼容 |
| Spring Cloud | 2023.0.0 | 2021.0.8 | 与Spring Boot 2.7.x兼容 |
| Spring Cloud Alibaba | 2022.0.0.0 | 2021.0.5.0 | 与Spring Cloud 2021.x兼容 |
| Jedis | 4.3.2 | 4.4.3 | 修复版本 |
| FastJSON | 2.0.4 | 2.0.40 | 修复版本 |

### 4. 验证方法

#### 使用Maven验证
```bash
# 验证父工程
mvn -f pom.xml validate

# 验证各服务
mvn -f gateway-service/pom.xml validate
mvn -f user-service/pom.xml validate
mvn -f auth-service/pom.xml validate
# ... 其他服务
```

#### 使用测试脚本
```bash
# 运行依赖检查脚本
./test-dependencies.bat
```

### 5. 注意事项

1. **Java版本**：保持Java 17，与Spring Boot 2.7.18兼容
2. **Maven版本**：建议使用Maven 3.6.3+
3. **IDE支持**：确保IDE支持Spring Boot 2.7.x
4. **依赖冲突**：如果出现依赖冲突，优先使用父工程管理的版本

### 6. 下一步

1. 安装Maven（如果未安装）
2. 运行依赖验证脚本
3. 启动各微服务进行测试
4. 配置Nacos、Redis、MySQL等基础设施

## 文件清单

- ✅ `pom.xml` - 父工程配置
- ✅ `gateway-service/pom.xml` - 网关服务
- ✅ `auth-service/pom.xml` - 认证服务
- ✅ `user-service/pom.xml` - 用户服务
- ✅ `post-service/pom.xml` - 动态服务
- ✅ `event-service/pom.xml` - 活动服务
- ✅ `chat-service/pom.xml` - 聊天服务
- ✅ `ai-audit-service/pom.xml` - AI审核服务
- ✅ `message-service/pom.xml` - 消息服务
- ✅ `recommend-service/pom.xml` - 推荐服务
- ✅ `test-dependencies.bat` - 依赖验证脚本
