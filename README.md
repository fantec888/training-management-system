# 物业管理系统实训 Demo

这是一个基于 `Spring Boot + Vue + Element Plus` 的企业级物业管理系统 Demo，已覆盖实训要求中的前端、后端、数据库、登录鉴权和主要业务模块。

## 项目结构

```text
training-management-system
├─ docs/                 # 作业文档、截图、说明材料
├─ src/
│  ├─ backend/           # Spring Boot 后端项目
│  └─ frontend/          # Vue + Element Plus 前端项目
├─ README.md
├─ task_plan.md
├─ progress.md
└─ findings.md
```

## 技术栈

- 后端：JDK 17、Spring Boot 3.5.16、Maven Wrapper、MyBatis、JWT
- 前端：Node.js、Vue 3、Vite、Vue Router、Element Plus、Axios
- 数据库：本地开发默认使用 H2 文件数据库，并开启 MySQL 兼容模式
- 远程数据库：老师提供 MySQL 地址后，可使用 `src/backend/src/main/resources/application-mysql.properties` 切换

## 已完成模块

- 首页仪表盘：经营指标、营收趋势、工单分布、待办任务、最新公告、模块联动
- 住户管理：业主/租户档案、联系方式、认证状态、标签、增删改查基础流程
- 楼栋房屋：楼栋资源、房屋列表、入住率、房屋绑定住户、状态管理
- 报修工单：工单编号、区域、优先级、处理人、状态流转、耗时统计
- 收费管理：应收、实收、欠费统计、账单创建、缴费确认、逾期标记
- 停车管理：车位统计、固定车/临停车、车牌和费用
- 公告活动：公告、活动、安全提醒、触达范围、发布流程
- 系统用户：后台账号、角色、部门、权限范围、启用/冻结

## 演示账号

```text
账号：admin
密码：123456
```

## 启动方式

### 1. 启动后端

```powershell
cd E:\企业实训\training-management-system\src\backend
.\mvnw.cmd spring-boot:run
```

后端地址：

```text
http://127.0.0.1:8080
```

健康检查：

```text
http://127.0.0.1:8080/api/health
```

H2 数据库控制台：

```text
http://127.0.0.1:8080/h2-console
```

H2 连接信息：

```text
JDBC URL: jdbc:h2:file:./data/training_management
User Name: sa
Password: 留空
```

### 2. 启动前端

```powershell
cd E:\企业实训\training-management-system\src\frontend
npm.cmd run dev -- --host 127.0.0.1
```

前端地址：

```text
http://127.0.0.1:5173
```

## 后端接口

登录后需要在请求头中携带：

```text
Authorization: Bearer <token>
```

主要接口：

```text
POST  /api/auth/login
GET   /api/auth/me
GET   /api/dashboard
GET   /api/residents
GET   /api/properties
GET   /api/repairs
GET   /api/billing
GET   /api/parking
GET   /api/notices
GET   /api/system-users
PATCH /api/system-users/{id}/status?enabled=true
```

## 数据库说明

本地 Demo 默认使用 H2 文件数据库，启动后会自动执行：

```text
src/backend/src/main/resources/schema.sql
src/backend/src/main/resources/data.sql
```

这些脚本会创建并填充以下表：

```text
sys_user
resident
building
room
repair_order
bill
parking_vehicle
notice
monthly_revenue
todo_item
```

如果老师给了远程 MySQL 数据库，需要修改：

```text
src/backend/src/main/resources/application-mysql.properties
```

把里面的连接地址、用户名、密码改成老师给的信息，例如：

```properties
spring.datasource.url=jdbc:mysql://老师给的IP:3306/training_management?useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true&useSSL=false
spring.datasource.username=老师给的用户名
spring.datasource.password=老师给的密码
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

然后使用 MySQL profile 启动：

```powershell
cd E:\企业实训\training-management-system\src\backend
.\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
```

详细说明见：

```text
docs/mysql-setup.md
```

## 验证结果

已完成以下验证：

- `src/backend`：`.\mvnw.cmd -q test` 通过
- `src/frontend`：`npm.cmd run build` 通过
- 登录接口通过，演示账号可获取 JWT
- 8 个业务模块接口均返回 `code=200`
