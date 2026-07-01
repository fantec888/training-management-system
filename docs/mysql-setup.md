# MySQL 远程数据库切换说明

本项目默认使用 H2 文件数据库，适合本地演示。老师提供远程 MySQL 后，可以切换到 `mysql` profile。

## 1. 创建数据库

先在 MySQL 中创建数据库：

```sql
CREATE DATABASE training_management
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;
```

## 2. 修改连接配置

打开：

```text
src/backend/src/main/resources/application-mysql.properties
```

把下面几项改成老师给你的信息：

```properties
spring.datasource.url=jdbc:mysql://老师给的IP:3306/training_management?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=老师给的用户名
spring.datasource.password=老师给的密码
```

## 3. 用 MySQL 启动后端

```powershell
cd E:\企业实训\training-management-system\src\backend
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
```

如果远程数据库是空库，第一次初始化前可以临时把 `application-mysql.properties` 改成：

```properties
spring.sql.init.mode=always
```

启动后会自动执行：

```text
src/backend/src/main/resources/schema.sql
src/backend/src/main/resources/data.sql
```

这些脚本会创建表并插入演示数据。初始化成功后，建议改回：

```properties
spring.sql.init.mode=never
```

## 4. 验证数据库连接

浏览器访问：

```text
http://127.0.0.1:8080/api/health
```

然后用前端登录：

```text
账号：admin
密码：123456
```

如果登录成功，并且首页、住户、收费等页面能读取数据，就说明后端已经连上 MySQL。

## 5. 注意事项

- 如果远程数据库已经有正式数据，必须保持：

```properties
spring.sql.init.mode=never
```

- 如果连接失败，优先检查 IP、端口、用户名、密码、防火墙和 MySQL 是否允许远程连接。
