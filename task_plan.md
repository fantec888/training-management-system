# 物业管理系统全量开发计划

## Goal

把当前物业管理系统 Demo 推进为带数据库、后端接口、前端接口调用和企业级白蓝色管理后台 UI 的完整实训项目。

当前代码目录已整理为：

- `src/backend`：Spring Boot 后端
- `src/frontend`：Vue + Element Plus 前端
- `docs`：实训作业、截图和说明文档

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
- Phase 6: Repository structure cleanup - complete
- Phase 7: Role permissions, CRUD completion, MySQL profile, and UI polish - complete
- Phase 8: 0703 RBAC homework framework upgrade and Word deliverable - complete

## Decisions

- Use existing Spring Boot + Vue + Element Plus structure.
- Put frontend and backend source projects under the root `src` folder.
- Keep local H2 database for development, with MySQL-compatible SQL where practical.
- Keep MySQL configuration template in `src/backend/src/main/resources/application.properties` for classroom remote database switching.
- Use authenticated `/api/**` endpoints except `/api/auth/login` and `/api/health`.
- Use focused list/update APIs that are enough for classroom demo and future CRUD expansion.
- Use lightweight role annotations for classroom-friendly permission control.
- Keep H2 as the default local database and provide a separate MySQL profile for remote database switching.
- For 0703 homework, implement database-backed RBAC with menu and button permissions instead of relying only on hardcoded frontend role maps.
- Keep PageHelper usage visible through a simple resident pagination endpoint.
- Keep MyBatis-Plus usage focused on RBAC role management to avoid rewriting existing MyBatis mappers.

## Verification

- Backend unit/context test passed with `src\backend\mvnw.cmd -q test`.
- Frontend production build passed with `src\frontend\npm.cmd run build`.
- Local backend can start on `http://127.0.0.1:8080`.
- Local frontend can start on `http://127.0.0.1:5173`.
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
- `POST /api/auth/login` -> `code=200`
- `GET /api/rbac/current` -> admin has 8 menus and 18 buttons
- `GET /api/rbac/roles` -> 4 roles
- `GET /api/rbac/permissions` -> 26 permissions
- `GET /api/residents/page?pageNum=1&pageSize=3` -> total 6, records 3
- `GET /v3/api-docs` -> OpenAPI title returned
- 0703 homework document generated at `每日作业\0703作业\0703作业-RBAC权限体系实现.docx`

## Errors Encountered

| Error | Attempt | Resolution |
|---|---|---|
| `GET /api/dashboard` returned 500 because H2 treated `value` alias poorly in a grouped query | Re-ran integration endpoint checks after backend API work | Changed SQL alias to `item_count` and normalized it back to `{ label, value }` in `DashboardService` |
| Port `5173` was occupied by an old Vite process from another project path | Checked process command line | Stopped the old process and restarted frontend from `E:\企业实训\training-management-system\src\frontend` |
| In-app browser automation was unavailable in the current session | Loaded browser plugin troubleshooting and listed available browsers | Browser list was empty, so verification continued through build, HTTP interface checks, and local service startup |
