# API 接口文档

后端基础地址：`http://127.0.0.1:8080`

## 认证

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| POST | `/api/auth/login` | 登录，返回 JWT 和当前用户权限 |
| GET | `/api/auth/me` | 获取当前用户信息 |

## 系统权限

| 方法 | 地址 | 说明 |
| --- | --- | --- |
| GET | `/api/rbac/current` | 当前用户菜单和按钮权限 |
| GET | `/api/rbac/roles` | 角色列表 |
| POST | `/api/rbac/roles` | 创建角色 |
| PUT | `/api/rbac/roles/{id}` | 更新角色 |
| GET | `/api/rbac/permissions` | 权限列表 |
| PUT | `/api/rbac/users/{userId}/roles` | 分配用户角色 |
| PUT | `/api/rbac/roles/{roleId}/permissions` | 分配角色权限 |

## 业务接口

| 模块 | 主要接口 |
| --- | --- |
| 小区管理 | `/api/communities` |
| 住户管理 | `/api/residents`、`/api/residents/page` |
| 楼栋房屋 | `/api/properties`、`/api/buildings`、`/api/rooms` |
| 报修工单 | `/api/repairs`、`/api/repairs/{id}/progress` |
| 投诉建议 | `/api/complaints`、`/api/complaints/{id}/reply` |
| 收费管理 | `/api/billing`、`/api/fee-items`、`/api/payments` |
| 停车管理 | `/api/parking` |
| 公告活动 | `/api/notices` |
| 智能服务 | `/api/smart-services`、`/api/activities`、`/api/packages` |
| 数据统计 | `/api/statistics/advanced` |
| 安防管理 | `/api/security`、`/api/access-controls`、`/api/patrols` |
| 系统用户 | `/api/system-users` |

## 进阶模块接口

| 方法 | 地址 | 说明 | 权限 |
| --- | --- | --- | --- |
| GET | `/api/smart-services` | 查询公告、社区活动、快递代收汇总数据 | 登录用户 |
| POST | `/api/activities` | 新增社区活动 | `button:activity:manage` |
| PUT | `/api/activities/{id}` | 编辑社区活动 | `button:activity:manage` |
| PATCH | `/api/activities/{id}/status` | 修改活动状态 | `button:activity:manage` |
| DELETE | `/api/activities/{id}` | 删除社区活动 | `button:activity:manage` |
| POST | `/api/packages` | 登记快递代收 | `button:package:manage` |
| PUT | `/api/packages/{id}` | 编辑快递信息 | `button:package:manage` |
| PATCH | `/api/packages/{id}/status` | 修改快递领取状态 | `button:package:manage` |
| DELETE | `/api/packages/{id}` | 删除快递记录 | `button:package:manage` |
| GET | `/api/statistics/advanced` | 查询入住率、报修、缴费率、投诉处理统计 | 登录用户 |
| GET | `/api/security` | 查询门禁、车辆、巡检综合数据 | 登录用户 |
| POST | `/api/access-controls` | 新增门禁设备 | `button:access:manage` |
| PUT | `/api/access-controls/{id}` | 编辑门禁设备 | `button:access:manage` |
| PATCH | `/api/access-controls/{id}/status` | 修改门禁在线状态 | `button:access:manage` |
| DELETE | `/api/access-controls/{id}` | 删除门禁设备 | `button:access:manage` |
| POST | `/api/patrols` | 新增巡检任务 | `button:patrol:manage` |
| PUT | `/api/patrols/{id}` | 编辑巡检任务 | `button:patrol:manage` |
| PATCH | `/api/patrols/{id}/finish` | 完成巡检并填写结果 | `button:patrol:manage` |
| DELETE | `/api/patrols/{id}` | 删除巡检任务 | `button:patrol:manage` |

## 返回格式

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

分页接口返回：

```json
{
  "records": [],
  "total": 100,
  "pageNum": 1,
  "pageSize": 10,
  "pages": 10
}
```

Swagger 地址：`http://127.0.0.1:8080/swagger-ui/index.html`。
