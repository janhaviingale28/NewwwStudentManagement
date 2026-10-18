# Student Management System — Spring Boot + PostgreSQL

Java 17, Spring Boot 3.4.5, Spring Web, Spring Data JPA, PostgreSQL, JUnit 5.

## Features
- Hibernate automatically creates/updates `students`, `student_profiles`, and `departments` tables (`ddl-auto=update`).
- One-to-one: student + profile are saved together.
- Many-to-one/one-to-many: each student must refer to a valid department.
- Four default departments are inserted on first startup only if the department table is empty. Always get their actual IDs from `GET /api/departments`.
- Student and Department CRUD endpoints, required-field validation, and duplicate-email protection.
- Kept simple: no custom `ApiExceptionHandler` or `StudentNotFoundException` classes. If a student ID does not exist, Java’s built-in `NoSuchElementException` is thrown; Spring Boot handles the default error response.
- A department cannot be deleted while students are assigned to it.

## Setup
1. In pgAdmin create database `student_db`.
2. In `src/main/resources/application.properties`, replace `CHANGE_ME` with your PostgreSQL password. Or set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` environment variables.
3. Import this folder into Eclipse using **Existing Maven Project** and use JDK 17.
4. Run `StudentManagementSystemApplication.java` as Java Application.
5. Refresh pgAdmin > `student_db` > Schemas > public > Tables.

Maven command: `mvn test` / `mvn spring-boot:run`. Maven was not installed in the environment used to prepare this ZIP, so automated tests could not be executed here.

## Postman
Base URL: `http://localhost:8080`; header `Content-Type: application/json`.

First call `GET /api/departments` and use a real department ID in student requests. Example `POST /api/students`:
```json
{
  "name": "Janhavi Ingale",
  "email": "janhavi.ingale@example.com",
  "course": "Java Full Stack",
  "profile": { "address": "Ahmedabad, Gujarat", "phone": "9876543210" },
  "department": { "id": 1 }
}
```
Student endpoints: `GET /api/students`, `GET /api/students/{id}`, `POST /api/students`, `PUT /api/students/{id}`, `DELETE /api/students/{id}`.
Department endpoints: `GET /api/departments`, `GET /api/departments/{id}`, `POST /api/departments`, `PUT /api/departments/{id}`, `DELETE /api/departments/{id}`.

Student POST/PUT requires `name`, valid unique `email`, `course`, `profile.address`, `profile.phone`, and `department.id`. Missing values and unknown department IDs are rejected rather than silently stored as NULL. Use the real department ID from GET; IDs are not guaranteed to be 1.

## Verify records in pgAdmin
```sql
SELECT * FROM departments ORDER BY id;
SELECT s.id, s.full_name, s.email, s.course, s.department_id, d.department_name, s.profile_id
FROM students s LEFT JOIN departments d ON d.id = s.department_id ORDER BY s.id;
SELECT s.id AS student_id, s.full_name, p.id AS profile_id, p.address, p.phone
FROM students s LEFT JOIN student_profiles p ON p.id = s.profile_id ORDER BY s.id;
SELECT * FROM students WHERE department_id IS NULL OR profile_id IS NULL;
SELECT * FROM student_profiles WHERE address IS NULL OR phone IS NULL;
```
`ddl-auto=update` does not automatically repair old records that already contain NULL values. Existing incomplete data must be reviewed and corrected; real profile address/phone values cannot safely be invented.
