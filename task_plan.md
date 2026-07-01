# 物业管理系统全量开发计划

## Goal

把当前物业管理系统 Demo 推进为带数据库、后端接口、前端接口调用和企业级白蓝色管理后台 UI 的完整实训项目。

覆盖模块：

- 首页仪表盘
- 住户管理
- 楼栋房屋
- 报修工单
- 收费管理
- 停车管理
- 公告活动
- 系统用户

## Status

- Phase 1: Context review - complete
- Phase 2: Database schema and seed data - complete
- Phase 3: Backend APIs for all modules - complete
- Phase 4: Frontend API integration for all pages - complete
- Phase 5: Verification and local run - complete

## Decisions

- Use existing Spring Boot + Vue + Element Plus structure.
- Keep local H2 database for development, with MySQL-compatible SQL where practical.
- Keep MySQL configuration template in `application.properties` for classroom remote database switching.
- Use authenticated `/api/**` endpoints except `/api/auth/login` and `/api/health`.
- Use focused list/update APIs that are enough for classroom demo and future CRUD expansion.

## Verification

- Backend unit/context test passed with `backend\mvnw.cmd -q test`.
- Frontend production build passed with `frontend\npm.cmd run build`.
- Local backend started on `http://127.0.0.1:8080`.
- Local frontend started on `http://127.0.0.1:5173`.
- Login verified with `admin / 123456`.
- Module APIs verified:
  - `GET /api/dashboard` -> `code=200`
  - `GET /api/residents` -> `code=200`
  - `GET /api/properties` -> `code=200`
  - `GET /api/repairs` -> `code=200`
  - `GET /api/billing` -> `code=200`
  - `GET /api/parking` -> `code=200`
  - `GET /api/notices` -> `code=200`
  - `GET /api/system-users` -> `code=200`

## Errors Encountered

| Error | Attempt | Resolution |
|---|---|---|
| `GET /api/dashboard` returned 500 because H2 treated `value` alias poorly in a grouped query | Re-ran integration endpoint checks after backend API work | Changed SQL alias to `item_count` and normalized it back to `{ label, value }` in `DashboardService` |
| Port `5173` was occupied by an old Vite process from another project path | Checked process command line | Stopped the old process and restarted frontend from `E:\企业实训\training-management-system\frontend` |
| In-app browser automation was unavailable in the current session | Loaded browser plugin troubleshooting and listed available browsers | Browser list was empty, so verification continued through build, HTTP interface checks, and local service startup |

