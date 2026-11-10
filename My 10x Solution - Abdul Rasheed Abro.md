# My 10x Solution - Abdul Rasheed Abro

## 1. What problem am I solving?

Job seekers often apply to several companies at the same time. They may keep company names, job titles, application dates, interview updates and follow-up reminders in separate notes, spreadsheets or email conversations. This makes it difficult to see the current status of every application and remember when to follow up.

My solution is a **Job Application Tracker**. It provides one place to store application details, update progress and generate a report. The project focuses on organising a job seeker's own application records; it does not automatically access LinkedIn accounts or submit applications.

**10x claim:** The system reduces the effort of finding and reviewing application progress by keeping records, statuses and follow-up dates in one searchable API-backed place, with a PDF report generated on demand.

**Who has this problem?** Students, fresh graduates and job seekers managing multiple applications.

**Non-goal:** I will not build automatic LinkedIn login, scraping of private account data, or automatic job applications as part of this capstone.

## 2. How did I implement my solution?

I implemented the backend using Java 17 and Spring Boot. The REST API accepts JSON requests, validates required fields and returns appropriate HTTP responses. Spring Data JPA stores users, authentication tokens and job applications in an H2 file database, so records remain available after restarting the application.

A user first registers or logs in. The application hashes passwords with BCrypt and returns an expiring Bearer token. The token is required for application and reporting endpoints. Each application is associated with its owner, and users cannot read, update or delete another user's records.

The application supports creating, listing, updating and deleting job applications. Each record can contain a company, job title, job URL, status, applied date, follow-up date and notes. A scheduled background task periodically checks for due follow-ups and writes a count to the application log. A PDFBox service creates a downloadable report of the user's latest application records. The summary endpoint uses caching to reuse calculated totals; changes to the user's applications invalidate that user's cached summary.

### Program concepts implemented

| Concept | Where it is implemented |
|---|---|
| API endpoints | `controller/` — authentication, application CRUD and reporting routes |
| Database | `model/` and `repository/` — JPA entities stored in H2 |
| Authentication | `service/AuthService.java`, `security/` — BCrypt and Bearer token protection |
| Background jobs | `service/FollowUpScheduler.java` — scheduled follow-up scan |
| Reporting (PDF) | `service/PdfReportService.java` and `/api/reports/applications.pdf` |
| Caching logic | `service/JobService.java` — cached summary and cache eviction on changes |

**Swaps:** None. All six concepts are selected from the capstone's primary concept list.

## 3. How to run the project

### Requirements
- JDK 17 or later
- Maven 3.9 or later
- Postman, Insomnia or curl for API testing

### Start
1. Extract the project ZIP.
2. Open a terminal in the project folder.
3. Run `mvn spring-boot:run`.
4. The API will start at `http://localhost:8080`.

### Demo steps
1. Send `POST /api/auth/register` with `{"email":"demo@example.com","password":"DemoPass123"}`.
2. Copy the returned token.
3. Send `POST /api/applications` with a Bearer token and a JSON application record.
4. Send `GET /api/applications` to view saved applications.
5. Send `GET /api/reports/summary` to view totals.
6. Send `GET /api/reports/applications.pdf` to download the PDF report.
7. Restart the application and list applications again to confirm that data persists.

The full endpoint list, request examples and project structure are documented in the repository README.

## 4. Scope and responsible use

The project uses local demo data and free development tools. No payment details or API keys are required. No secrets or real third-party personal data should be committed to the repository. The current background job logs due follow-ups rather than sending messages, keeping the first version small and testable.
