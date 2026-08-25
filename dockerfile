FROM eclipse-temurin:21-jre
WORKDIR /app

# Копируем корневой pom и дочерний модуль для оптимизации кеша
COPY pom.xml .
COPY authGateway/pom.xml authGateway/
COPY authGateway/src authGateway/src

# Собираем только authGateway
RUN mvn clean package -pl authGateway -am -DskipTests

# Этап запуска (Run Stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/authGateway/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]