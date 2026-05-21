FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw && ./mvnw -B -DskipTests dependency:go-offline

COPY src/ src/

RUN ./mvnw -B -DskipTests package && cp target/Warehouse-Workshop-0.0.1-SNAPSHOT.jar app.jar

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S app && adduser -S app -G app

COPY --from=build /workspace/app.jar app.jar

USER app

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
