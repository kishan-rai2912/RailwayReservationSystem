# RailwayReservationSystem

College/demo railway reservation application using Java 24, Jakarta Servlet 6.0 (`jakarta.servlet`), JSP/JSTL 3.0, Tomcat 10.1, Maven WAR, MySQL 8, JDBC and BCrypt. Demo only: no real railway inventory, payment, or official ticketing.

## Compatibility
- JDK 24; Apache Tomcat 10.1.x (Servlet 6.0, `jakarta.servlet.*`); Maven 3.9+; MySQL 8.x.
- Jakarta Servlet API 6.0.0 is `provided` and is not packaged in `WEB-INF/lib`. Jakarta JSTL API 3.0.0 and GlassFish JSTL implementation 3.0.1 are included for Tomcat 10.1.
- MySQL Connector/J 8.4.0 and jBCrypt 0.4 are Maven dependencies.

## Database setup (Windows PowerShell)
1. Install MySQL 8 and open MySQL Command Line Client or a terminal.
2. Run `mysql -u root -p < database/schema.sql`, then `mysql -u root -p < database/seed-data.sql`, then `mysql -u root -p < database/generate-schedules.sql`.
3. Create a least-privilege application account (choose your own password):

```sql
CREATE USER 'railway_app'@'localhost' IDENTIFIED BY 'REPLACE_WITH_A_LONG_LOCAL_PASSWORD';
GRANT SELECT, INSERT, UPDATE, DELETE ON railway_reservation.* TO 'railway_app'@'localhost';
FLUSH PRIVILEGES;
```

4. Set environment variables in the terminal used to start Eclipse/Tomcat, or in Windows Environment Variables:

```powershell
$env:DB_URL='jdbc:mysql://localhost:3306/railway_reservation?useSSL=false&serverTimezone=UTC'
$env:DB_USER='railway_app'
$env:DB_PASSWORD='YOUR_LOCAL_PASSWORD'
```
Do not commit secrets. Restart Eclipse after changing permanent Windows environment variables.

## Build and deploy
```powershell
java -version
mvn -version
mvn clean test package
```
The artifact is `target/RailwayReservationSystem.war`. In Eclipse: File > Import > Maven > Existing Maven Projects; select this folder. Configure Installed JREs to JDK 24, set Java compiler compliance to 24, and use Maven > Update Project. Under Project Properties > Targeted Runtimes, select the configured Tomcat 10.1 runtime. In Servers, add Tomcat 10.1 and add this project. Clean/publish, start server, open `http://localhost:8080/RailwayReservationSystem/`.

## Create the first administrator
Build the project, then run `com.railway.util.CreateAdmin` from a real terminal with the project's dependency classpath and `DB_URL`, `DB_USER`, `DB_PASSWORD` set. It prompts for admin details and password without echoing the password. In Eclipse, use Run Configurations > Java Application > Environment to set those variables. Do not put admin passwords in SQL or source control.

## Booking/cancellation policy
- Reservation requires an active train schedule for the selected date. Create schedules after adding trains using `database/generate-schedules.sql`.
- Up to 6 passengers per booking. Fare is train fare × passenger count; no payment is processed.
- Demo cancellation is permitted for any confirmed booking. It is a full demo cancellation with no monetary refund because there is no payment integration. The booking row is locked and only a successful CONFIRMED→CANCELLED transition releases seats.

## Test
`mvn clean test` runs unit/validation and early service validation tests. Database-backed integration tests require a running, dedicated test database and are not included as automatically runnable tests in this starter. Do not interpret a successful unit test run as full end-to-end verification.

## Feature scope / known limitations
Registration, login/logout, train search, passenger booking, owner-scoped booking detail/history, cancellation, admin dashboard and adding trains are wired. Train update/deactivation UI, passenger display in booking detail, per-schedule admin management, and full automated database/concurrency/HTTP integration tests remain follow-up work and are not claimed as implemented. The sample is a college demo, not production railway software.

## Troubleshooting
- **404:** confirm context path `/RailwayReservationSystem`, server deployment, and servlet mapping in `WEB-INF/web.xml`.
- **500 / DB unavailable:** check Tomcat console logs and `DB_URL`, `DB_USER`, `DB_PASSWORD`; ensure schema and schedules exist.
- **ClassNotFoundException / NoClassDefFoundError:** Maven > Update Project; inspect `WEB-INF/lib`; do not package `jakarta.servlet-api`; Tomcat provides it.
- **javax/jakarta conflict:** use Tomcat 10.1 and `jakarta.servlet` only; do not add `javax.servlet-api` or legacy JSTL dependencies.
- **JSP compilation errors:** check Tomcat logs and confirm the project targets Tomcat 10.1 and uses compatible Jakarta JSP syntax.
- **Port 8080 busy:** change the HTTP port in Eclipse Servers > Tomcat > Ports, then restart.
- **No trains:** generate schedules for the chosen date and ensure source/destination names match seeded data.

## Test plan / acceptance checklist
- [ ] Register, reject duplicate email, login with correct/incorrect password, logout.
- [ ] Validate malformed email, phone, age, passenger count, and past date.
- [ ] Search route with results and route with no results.
- [ ] Book valid trip; reject invalid train/date and insufficient seats.
- [ ] Run concurrent last-seat reservations and confirm seats never become negative.
- [ ] Force passenger insert failure and confirm transaction rollback restores seats.
- [ ] Cancel once and repeat; confirm seat release occurs exactly once.
- [ ] Test unauthenticated access, ordinary-user admin access, cross-user PNR access, and missing/invalid CSRF token.
- [ ] Test XSS-like names and verify they render escaped.
- [ ] Test invalid PNR, database outage, session expiry, 404/500, and invalid HTTP methods.
- [ ] Run `mvn clean test package` and deploy the WAR to Tomcat 10.1.

| Test | Expected result | PASS/FAIL | Notes |
|---|---|---|---|
| Maven unit tests | All supplied tests pass | | |
| WAR package | WAR created | | |
| Registration/login | Valid credentials accepted, hashes stored | | |
| Reservation/cancel | Atomic seat count, no double release | | |
| Access control/CSRF | Unauthorized requests rejected | | |
| Deployment | Home page loads from Tomcat 10.1 | | |


## JDK 24 / Tomcat 10.1 migration notes
- All servlet/filter imports use `jakarta.servlet.*`; no `javax.servlet` API is used.
- `WEB-INF/web.xml` uses the Jakarta EE 10 / Servlet 6.0 descriptor namespace and schema.
- Maven compiler release is 24. Use an actual JDK 24 installation to build; an older JDK will report `release version 24 not supported`.
- Tomcat 10.1 provides Servlet 6.0 and Jakarta Pages 3.1 APIs. JSTL dependencies are Jakarta JSTL 3.x.
- This project still uses some JSP scriptlets in legacy presentation pages; the Jakarta-compatible JSTL dependencies are available, but not every scriptlet has been rewritten to JSTL tags.
- The ZIP is a source-project migration. Execute `mvn clean test package` using JDK 24 and deploy to a local Tomcat 10.1 instance before treating it as verified.
