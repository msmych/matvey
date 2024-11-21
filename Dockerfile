FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

ARG GH_PACKAGES_RO_TOKEN
ENV GH_PACKAGES_RO_TOKEN=$GH_PACKAGES_RO_TOKEN

COPY gradlew settings.gradle.kts build.gradle.kts gradle.properties /app/

COPY gradle /app/gradle
COPY server /app/server

RUN chmod +x gradlew

RUN ./gradlew server:shadowJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/server/build /app/build

EXPOSE 8443

CMD ["java", "-jar", "build/libs/server-0.1.0-all.jar"]
