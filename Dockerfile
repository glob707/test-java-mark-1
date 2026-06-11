FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .
COPY core/pom.xml core/
COPY ui/pom.xml ui/
COPY api/pom.xml api/
RUN mvn dependency:go-offline -q

RUN mvn exec:java -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install --with-deps chromium" -q

COPY core ./core
COPY ui ./ui
COPY api ./api

CMD ["mvn", "test", "-pl", "ui", "-Dheadless=true", "-q"]
