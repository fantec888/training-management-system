# Findings

## Current Project

- Project root: `E:\企业实训\training-management-system`
- Source layout:
  - Backend: `src/backend`
  - Frontend: `src/frontend`
  - Documents: `docs`
- Backend has login, JWT, auth interceptor, CORS config, H2 database, and seeded `sys_user` data.
- Frontend uses Vue 3, Element Plus, Vue Router, Axios, and a white/blue enterprise management UI.
- The project now uses real API calls for the required demo modules instead of only local mock data.

## Required Modules

- Dashboard
- Residents
- Properties / buildings / rooms
- Repairs
- Billing
- Parking
- Notices / activities
- System users

## Backend API Shape

- `POST /api/auth/login`
- `GET /api/auth/me`
- `GET /api/dashboard`
- `GET /api/residents`
- `GET /api/properties`
- `GET /api/repairs`
- `GET /api/billing`
- `GET /api/parking`
- `GET /api/notices`
- `GET /api/system-users`
- `PATCH /api/system-users/{id}/status?enabled=true|false`

## Database

- Local database is H2 file DB in MySQL compatibility mode:
  - `jdbc:h2:file:./data/training_management;MODE=MySQL;DATABASE_TO_LOWER=TRUE;AUTO_SERVER=TRUE`
- MySQL template is preserved in `src/backend/src/main/resources/application.properties`.
- Seed data covers residents, buildings, rooms, repair orders, bills, parking vehicles, notices, dashboard revenue trend, dashboard todo list, and system users.

## Final Verification

- Backend tests passed from `src/backend`.
- Frontend build passed from `src/frontend`.
- Login with `admin / 123456` works.
- All 8 required module APIs returned `code=200`.
