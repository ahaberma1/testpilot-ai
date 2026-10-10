# TestPilot AI

A beginner-friendly Java 21 backend built with Spring Boot, Maven, and JUnit 5.
This initial version provides a health endpoint. It requires no API keys or
external services and contains no AI integration, database, or frontend.

## Requirements

- JDK 21 (including `javac`, not just the Java runtime)
- Maven 3.6.3 or newer

Check your tools with `java -version`, `javac -version`, and `mvn -version`.
Maven should report Java 21. Set `JAVA_HOME` to your JDK directory if necessary.
The first build needs Internet access to download dependencies from Maven Central.

## Setup and run

Clone the repository and open a terminal in its root directory (where `pom.xml` is).

```sh
git clone https://github.com/ahaberma1/testpilot-ai.git
cd testpilot-ai
mvn clean verify
mvn spring-boot:run
```

The server starts on port 8080. Stop it with Ctrl+C. To use another port:

```sh
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

In a second terminal, request the health endpoint:

```sh
curl http://localhost:8080/api/health
```

Expected HTTP status: `200 OK`. Expected JSON:

```json
{"status":"UP","application":"TestPilot AI"}
```

This response shows the backend can handle requests; it does not check external services.

## Test and package

```sh
mvn test
mvn clean verify
java -jar target/testpilot-ai-0.0.1-SNAPSHOT.jar
```

`mvn test` runs the JUnit 5 test. `mvn clean verify` removes old build output,
compiles the project, runs the test, and creates an executable Spring Boot JAR.
Run the JAR as an alternative to `mvn spring-boot:run`.

## How the code is organized

All Java code uses the base package `com.testpilot.ai`. The `health` subpackage
keeps the endpoint and its response together. Each class has one responsibility:

- **`TestPilotAiApplication`** is the entry point. Java calls its `main` method.
  `SpringApplication.run` starts Spring Boot and the embedded web server.
  `@SpringBootApplication` enables configuration and finds components in this
  package and its subpackages.
- **`HealthController`** handles web requests. `@RestController` tells Spring to
  turn its returned objects into response bodies. `@GetMapping("/api/health")`
  connects GET requests to the `health` method, which creates the response object.
- **`HealthResponse`** holds the response data: `status` and `application`.
  It is a Java record, a concise immutable class. Java generates its constructor,
  accessors, and equality methods. Spring converts its fields into JSON. Keeping
  this data separate from request handling makes the API easy to understand.
- **`HealthControllerTest`** verifies the endpoint using JUnit 5. `@SpringBootTest`
  loads the real application configuration. Spring injects `MockMvc`, which sends
  a simulated HTTP request through Spring's request handling without opening a
  network port. The test checks HTTP 200, JSON content type, and the exact response
  fields and values. `@Test` marks the method that JUnit runs.

`pom.xml` defines Java 21, dependencies, and build plugins. Spring Boot manages
compatible library versions, including JUnit 5. `application.properties` sets the
internal application name. Maven writes generated files into `target/`.

The controller handles HTTP, the record represents data, and the application class
handles startup. A service layer can be introduced when business logic is added;
the fixed health response does not need one yet.
