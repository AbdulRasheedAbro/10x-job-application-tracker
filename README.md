# Job Application Tracker — Your 10x Solution

A Java REST API that helps a job seeker keep job applications, follow-up dates and progress in one place. It is a focused backend capstone based on the problem of tracking applications manually across notes, spreadsheets and email.

> **Important:** This project is a tracker and assistant, not an automated LinkedIn bot. It does not log in to LinkedIn, scrape private data or submit applications on a user's behalf.

## 10x claim
Instead of repeatedly searching notes and spreadsheets, a user can create, update and review an application through one authenticated API and export a progress report in seconds.

## Implemented program concepts

| Concept | Implementation |
|---|---|
| API endpoints | REST endpoints under `/api` with JSON responses, validation and HTTP status codes |
| Database | H2 file database with Spring Data JPA; data persists across restarts |
| Authentication | Register/login, BCrypt password hashing, expiring Bearer tokens, protected application/report routes |
| Background job | Scheduled follow-up scan logs due follow-ups at a configurable interval |
| PDF reporting | `/api/reports/applications.pdf` generates a downloadable PDF |
| Caching | `/api/reports/summary` caches each user's summary; create/update/delete evict that user's cache |

All six are from the primary concept list; no swaps are used.

## Technology
- Java 17
- Spring Boot 3.5.3
- Spring Web, Spring Data JPA, Bean Validation
- H2 database
- BCrypt password hashing
- Apache PDFBox
- Spring Cache and scheduled tasks
- Maven

## Run locally
Prerequisites: JDK 17+ and Maven 3.9+.

Windows PowerShell:
```powershell
mvn spring-boot:run
```
macOS/Linux:
```bash
mvn spring-boot:run
```
The API starts at `http://localhost:8080`. The H2 database is stored under `./data/` and is created automatically.

Optional environment variables:
- `PORT` (default `8080`)
- `DB_URL` (default `jdbc:h2:file:./data/jobtracker;AUTO_SERVER=TRUE`)
- `DB_USER` (default `sa`)
- `DB_PASSWORD` (default empty)

Do not put real passwords or secrets in source control. For a public deployment, configure a strong database password and HTTPS.

## 5-minute demo path

1. Register:
```http
POST /api/auth/register
Content-Type: application/json

{"email":"demo@example.com","password":"DemoPass123"}
```
2. Copy the `token` from the response.
3. Create an application (send `Authorization: Bearer YOUR_TOKEN`):
```http
POST /api/applications
Content-Type: application/json
Authorization: Bearer YOUR_TOKEN

{"company":"Example Software","jobTitle":"Junior Java Developer","jobUrl":"https://example.com/jobs/1","status":"APPLIED","appliedDate":"2026-11-10","followUpDate":"2026-11-12","notes":"Demo record"}
```
4. List applications: `GET /api/applications`.
5. View cached summary: `GET /api/reports/summary`.
6. Download PDF: `GET /api/reports/applications.pdf`.
7. Restart the app and list again to verify database persistence.

Use Postman, Insomnia or curl to send requests. A browser alone is not an API client for POST requests.

## API reference

| Method | Endpoint | Authentication |
|---|---|---|
| POST | `/api/auth/register` | No |
| POST | `/api/auth/login` | No |
| GET | `/api/applications` | Bearer token |
| POST | `/api/applications` | Bearer token |
| PUT | `/api/applications/{id}` | Bearer token |
| DELETE | `/api/applications/{id}` | Bearer token |
| GET | `/api/reports/summary` | Bearer token |
| GET | `/api/reports/applications.pdf` | Bearer token |

Application statuses: `SAVED`, `APPLIED`, `INTERVIEW`, `OFFER`, `REJECTED`, `WITHDRAWN`.

## Security and limitations
- Passwords are stored as BCrypt hashes, not plain text.
- Application records are scoped to the authenticated owner.
- Tokens expire after 24 hours.
- The scheduled job currently writes due-follow-up counts to the application log; it does not send email or modify application status.
- H2 is suitable for a free local demo. A production deployment should use managed database credentials, HTTPS, stronger token lifecycle controls and database backups.
- No real personal data is included. Use the seeded demo values above.

## 5-minute demo script
Register a demo user → create one APPLIED application with a follow-up date → list it → open summary → download PDF → restart app and confirm the record remains.

## Future ideas
Email reminders, a small web UI, CSV import/export, integration with a user's own job-board bookmarks, and optional AI-assisted job-description summarisation with explicit user review.

## Repository structure
```text
src/main/java/com/abdulrasheed/jobtracker/
  config/       Web filter registration
  controller/   REST endpoints and error handling
  dto/          Request/response records
  model/        JPA entities and status enum
  repository/   Spring Data repositories
  security/     Bearer token authentication
  service/      Business logic, PDF, scheduler
```

## Build verification
Run `mvn test` and `mvn package` before publishing. This generated starter package has not been run against your local Java/Maven installation; resolve any dependency or compiler issue reported by Maven before submission.
