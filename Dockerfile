FROM gradle:8.8.0-jdk21 AS build

WORKDIR /home/gradle/src

COPY build.gradle settings.gradle ./

RUN gradle build --no-daemon || return 0

COPY . .

RUN gradle build --no-daemon -x test

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080

CMD ["sh", "-c", "java -Dspring.profiles.active=prod -Dspring.datasource.url=$SPRING_DATASOURCE_URL -Dspring.datasource.username=$SPRING_DATASOURCE_USERNAME -Dspring.datasource.password=$SPRING_DATASOURCE_PASSWORD -jar app.jar"]