# URL shortener 

### Overview

This is a Spring Boot-based URL Shortener service that allows users to generate shortened URLs, retrieve the original URLs, and track analytics for shortened links.

#### Prerequisites

- Java 21 (Liberica JDK recommended)

- Gradle

- Docker

- PostgreSQL 17

### Setup and Installation

1. Clone the repository
```
git clone https://github.com/wyrdsister/URLshortener.git
cd URLshortener
```

2. Build the project
```
./gradlew clean build
```

4. Run with Docker
```
docker-compose up --build
```

6. Open Swagger UI of app1 in your browser
```
http://localhost:8080/swagger-ui/index.html#
```
or Swagger UI of app2 in your browser
```
http://localhost:8888/swagger-ui/index.html#
```

### REST API Endpoints

1. Shorten a URL

Endpoint: `POST /api/v1/shorten`

Request Body:
```
{
  "longUrl": "https://example.com/some-long-url"
}
```

Response:
```
{
  "shortUrl": "http://localhost:8080/xyz123",
  "shortCode": "xyz123",
  "expiredAt": "2025-12-31T23:59:59Z"
}
```

2. Redirect to Original URL

Endpoint: `GET /{shortCode}`

Example:
```curl -L http://localhost:8080/xyz123```

Redirects the user to the original URL.

3. Get Original URL Without Redirect

Endpoint: `GET /api/v1/url/{shortCode}`

Response:
```
{
  "longUrl": "https://example.com/some-long-url",
  "createdAt": "2025-01-01T12:00:00Z",
  "expiredAt": "2025-12-31T23:59:59Z"
}
```

4. Get Click Analytics

Endpoint: `GET /api/v1/analytics/{shortCode}`

Response:
```
{
  "shortCode": "xyz123",
  "clicksCount": 42
}
```
