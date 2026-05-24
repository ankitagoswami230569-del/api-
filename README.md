# BFHL — Bajaj Finserv Health Backend Qualifier API

A Spring Boot REST API that separates numbers and alphabets from an input array.

---

## Tech Stack

- Java 17
- Spring Boot 3.x
- Maven
- Lombok
- Spring Validation

---

## Project Structure

```
src/main/java/com/example/bfhl/
├── BfhlApplication.java
├── controller/
│   └── BfhlController.java
├── service/
│   └── BfhlService.java
├── model/
│   ├── RequestDto.java
│   └── ResponseDto.java
└── exception/
    └── GlobalExceptionHandler.java
```

---

## Running Locally

```bash
mvn spring-boot:run
```

The server starts on `http://localhost:8080`.

---

## API Endpoints

### GET / — Health Check

```bash
curl http://localhost:8080/
```

Response:
```json
{"message": "BFHL API Running"}
```

---

### POST /bfhl — Main Endpoint

```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{"data": ["A", "1", "334", "4", "R"]}'
```

Response:
```json
{
  "is_success": true,
  "user_id": "yourname_24052026",
  "email": "your_email@example.com",
  "roll_number": "YOURROLL123",
  "numbers": ["1", "334", "4"],
  "alphabets": ["A", "R"]
}
```

---

## Validation Rules

| Input   | Category  | Reason                        |
|---------|-----------|-------------------------------|
| `"1"`   | number    | matches `^\d+$`               |
| `"334"` | number    | matches `^\d+$`               |
| `"A"`   | alphabet  | matches `^[A-Za-z]+$`         |
| `"R"`   | alphabet  | matches `^[A-Za-z]+$`         |
| `"A1"`  | ignored   | mixed alphanumeric             |
| `"@"`   | ignored   | special character              |
| `"12B"` | ignored   | mixed alphanumeric             |

---

## Error Responses

**Null data field:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d '{}'
```
```json
{
  "is_success": false,
  "errors": {"data": "data field must not be null"}
}
```

**Malformed JSON:**
```bash
curl -X POST http://localhost:8080/bfhl \
  -H "Content-Type: application/json" \
  -d 'not-json'
```
```json
{
  "is_success": false,
  "error": "Invalid JSON format: ..."
}
```

---

## Postman Testing Steps

1. Open Postman and create a new request.
2. Set method to **POST**, URL to `http://localhost:8080/bfhl`.
3. Go to **Body** → **raw** → select **JSON**.
4. Paste:
   ```json
   {"data": ["A", "1", "334", "4", "R"]}
   ```
5. Click **Send**. Expect a `200 OK` with the separated response.

---

## Customising User Details

Edit `src/main/resources/application.properties`:

```properties
bfhl.user.id=yourname_24052026
bfhl.user.email=your_email@example.com
bfhl.user.roll-number=YOURROLL123
```

---

## Running Tests

```bash
mvn test
```

---

## Deployment

### Render

1. Push the project to a GitHub repository.
2. Go to [render.com](https://render.com) → **New Web Service**.
3. Connect your GitHub repo.
4. Set:
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/bfhl-1.0.0.jar`
   - **Environment:** Java 17
5. Click **Deploy**.

### Railway

1. Push the project to GitHub.
2. Go to [railway.app](https://railway.app) → **New Project** → **Deploy from GitHub**.
3. Select your repo. Railway auto-detects Maven and builds it.
4. Set the `PORT` environment variable if needed (Spring Boot reads `SERVER_PORT`).
5. Deploy.

> **Tip:** For both platforms, set environment variables for `BFHL_USER_ID`, `BFHL_USER_EMAIL`, and `BFHL_USER_ROLL_NUMBER` instead of hardcoding them in `application.properties`.
