# API testing examples

Replace `TOKEN` with the token returned by registration/login.

## Register
```bash
curl -X POST http://localhost:8080/api/auth/register -H "Content-Type: application/json" -d '{"email":"demo@example.com","password":"DemoPass123"}'
```

## Create an application
```bash
curl -X POST http://localhost:8080/api/applications \
  -H "Content-Type: application/json" -H "Authorization: Bearer TOKEN" \
  -d '{"company":"Example Software","jobTitle":"Java Intern","status":"APPLIED","appliedDate":"2026-11-10","followUpDate":"2026-11-12","notes":"Demo"}'
```

## List and report
```bash
curl http://localhost:8080/api/applications -H "Authorization: Bearer TOKEN"
curl http://localhost:8080/api/reports/summary -H "Authorization: Bearer TOKEN"
curl http://localhost:8080/api/reports/applications.pdf -H "Authorization: Bearer TOKEN" --output report.pdf
```
