# --- Etap 1: Budowanie Aplikacji ---
# Używamy oficjalnego obrazu Gradle z wymaganą wersją JDK (21).
# Używamy wersji Gradle 8.8.0, która jest kompatybilna z pluginem Vaadin (na wszelki wypadek).
FROM gradle:8.8.0-jdk21 AS build

# Ustawiamy katalog roboczy wewnątrz kontenera budującego.
WORKDIR /home/gradle/src

# Kopiujemy tylko pliki definiujące zależności.
# To jest optymalizacja - Docker będzie cache'ował ten krok,
# dopóki te pliki się nie zmienią, co przyspiesza kolejne buildy.
COPY build.gradle settings.gradle ./

# Pobieramy zależności, aby były dostępne w cache.
# "|| return 0" sprawia, że build nie zatrzyma się, jeśli nie ma zależności do pobrania.
RUN gradle build --no-daemon || return 0

# Kopiujemy resztę kodu źródłowego aplikacji.
COPY . .

# Budujemy aplikację za pomocą Gradle, pomijając testy (-x test).
RUN gradle build --no-daemon -x test


# --- Etap 2: Uruchomienie Aplikacji ---
# Używamy lekkiego, oficjalnego obrazu z samą maszyną wirtualną Javy (JRE),
# aby finalny obraz był jak najmniejszy.
FROM eclipse-temurin:21-jre-jammy

# Ustawiamy katalog roboczy wewnątrz finalnego kontenera.
WORKDIR /app

# Kopiujemy zbudowany plik .jar z etapu "build" do naszego finalnego obrazu.
# Używamy gwiazdki (*.jar), aby uniknąć problemów z dokładną nazwą wersji.
# Docker znajdzie jedyny plik .jar w tym folderze.
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar

# Ustawiamy port, na którym aplikacja backendowa będzie nasłuchiwać.
# Render użyje tej informacji.
EXPOSE 8080

# Komenda, która zostanie wykonana przy starcie kontenera.
# Uruchamia aplikację.
ENTRYPOINT ["java", "-jar", "app.jar"]
