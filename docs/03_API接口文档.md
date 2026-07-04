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
| 系统用户 | `/api/system-users` |

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
