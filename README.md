## CrownClinic Backend

Backend aplikacji CrownClinic, zbudowany jako serwis REST-owy przy użyciu **Spring Boot 3.5** z uwierzytelnianiem opartym na **JWT** oraz integracją z **OAuth2 (Google)**.

System obsługuje pacjentów, lekarzy, wizyty oraz zewnętrzne serwisy, takie jak Google Calendar i OpenWeatherMap.
Frontend repozytorium - https://github.com/Widlakolak/crownclinic-frontend

---

## 📄 Technologie i zależności

- **Java 21**
- **Spring Boot 3.5**
  - Spring Web, Data JPA, Security, OAuth2 Client, Session JDBC
- **Hibernate & PostgreSQL** (Render) / **H2** (lokalnie)
- **JWT** (`io.jsonwebtoken`)
- **Google Calendar API**, **OpenWeatherMap API**
- **SpringDoc / Swagger UI**
- **Gradle** (system budowania)
- **Docker** (do konteneryzacji)

---

## 🏃‍♂️ Jak uruchomić

```
Konto google do testowania funkcjonalności:
Login - crownclinictest@gmail.com
Hasło - crownclinictest1206

W widoku kalendarza przy dodawaniu wizyty trzeba wypełnić wszystkie pola, po dodaniu pacjenta trzeba kliknąć na puste pole - będzie widoczny ptaszek zaznaczenia i dodanie pacjenta będzie skuteczne.
```

```
Zalecane uruchomienie na Render

Frontend: https://crownclinic-frontend.onrender.com
Backend (Swagger UI): https://crownclinic-backend.onrender.com/swagger-ui.html

> ⚠ Uwaga: Aplikacja może wczytywać się dłużej (nawet 30 sekund) ze względu na darmowy hosting Render (Free Tier), który usypia instancje przy bezczynności.
```

```bash
# 1. Klonuj repozytorium
git clone https://github.com/twoj-uzytkownik/crownclinic-frontend.git
cd crownclinic-frontend

# 2. Uruchom aplikację (Vaadin + Spring Boot)
mvn spring-boot:run

# Domyślny adres: http://localhost:8080
```

---


## 🔹 Główne encje i relacje

- `User` — użytkownik systemu (rola: lekarz, recepcja, admin)
- `Patient` — pacjent
- `Appointment` — wizyta (połączona z `User` i `Patient`)
- `DoctorAvailability`, `MedicalRecord`, `Conversation`, `Message`, `Attachment` — dodatkowe funkcjonalności

> 📃 Dodatkowo tworzona jest tabela `SPRING_SESSION` i `SPRING_SESSION_ATTRIBUTES` (OAuth2).

---

## 🔗 Endpointy REST

Interaktywna dokumentacja: `/swagger-ui.html`

- `/auth` — logowanie JWT, rejestracja, Google OAuth2
- `/api/users` — CRUD użytkowników, `/me` (dane zalogowanego)
- `/api/patients` — CRUD pacjentów
- `/appointments` — CRUD wizyt + integracja z Google Calendar
- `/api/weather` — dane pogodowe (OpenWeatherMap)

---

## 📈 Roadmap (Planowane funkcje)

- Powiadomienia SMS/E-mail (przypomnienia)
- Wewnętrzny komunikator (wiadomości, rozmowy)
- Grafik lekarzy w panelu
- Kalendarz dla recepcji z widokiem wszystkich wizyt
- Pokrycie testami jednostkowymi i integracyjnymi

---

## ✉ Kontakt

Zespół CrownClinic Java Developers (2025)

---

## 🗓 Licencja

Projekt objęty licencją MIT. Zobacz plik [LICENSE](LICENSE).

