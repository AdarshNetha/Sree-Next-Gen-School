FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -DskipTests package \
    && cp "$(find target -maxdepth 1 -name '*.jar' ! -name '*.original' -print -quit)" app.jar

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl \
    && rm -rf /var/lib/apt/lists/*

# Create non-root user
RUN groupadd --system spring \
    && useradd --system --gid spring --create-home --home-dir /home/spring spring

COPY --from=build /app/app.jar ./app.jar

# Give ownership to the non-root user
RUN chown spring:spring /app/app.jar

# Run as non-root
USER spring

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]