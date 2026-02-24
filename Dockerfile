FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

# Копируем Gradle wrapper
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

# Кэшируем зависимости
RUN ./gradlew dependencies --no-daemon || true

# Копируем исходники
COPY src src

# Собираем JAR
RUN ./gradlew bootJar --no-daemon

# Runtime stage
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Создаём пользователя
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring

# Копируем JAR
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

# Оптимизация для контейнера
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]