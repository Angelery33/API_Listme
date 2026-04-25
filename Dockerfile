# Build stage
FROM gradle:8.10.2-jdk21-alpine AS build
WORKDIR /app
COPY build.gradle settings.gradle* ./
COPY src ./src
RUN gradle bootJar --no-daemon

# Runtime stage (reuses build image to avoid pulling from Docker Hub)
FROM gradle:8.10.2-jdk21-alpine
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8089
ENTRYPOINT ["java", "-jar", "app.jar"]