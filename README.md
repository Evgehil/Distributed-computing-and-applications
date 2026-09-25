# Лабораторная работа №3. RestTemplate между сервисами

Убрать прямой доступ `report-service` к БД. Данные — через HTTP-вызовы (RestTemplate) к `computer-store`.

## Стек

- Java 17, Spring Boot 3.2.5, Gradle 8.5
- Spring Cloud Gateway 2023.0.1
- Spring Web (RestTemplate)
- PostgreSQL 16, Liquibase
- Docker / Docker Compose

## Архитектура

```
Postman → Gateway (8080) ──┬──> computer-store (8081) ──[JPA]──> PostgreSQL
                           └──> report-service (8082) ──[RestTemplate]──> computer-store
```

- **computer-store** — CRUD, владелец данных, применяет миграции Liquibase
- **report-service** — только чтение; **не имеет JDBC**, получает данные через RestTemplate
- **gateway** — единая точка входа, маршрутизация
- **PostgreSQL** — общая БД (используется только computer-store)

## Что изменилось относительно ЛР2

| Аспект | ЛР2 | ЛР3 |
|---|---|---|
| Доступ к БД у report-service | Есть (JPA) | **Нет** |
| Как получает данные | JPA-запрос | **RestTemplate (HTTP)** |
| Драйвер PostgreSQL | Есть | **Удалён** |
| DataSource | Есть | **Удалён** |
| Модель `Computer` | JPA-сущность | **DTO** |

## Функционал

### computer-store
- Принять новый компьютер
- Оформить продажу
- Отредактировать информацию
- Отчёт: сколько имеется и сколько продано

### report-service (через RestTemplate)
- Список всех компьютеров
- Только доступные
- Только проданные
- Сводка

## Запуск

```bash
docker compose up --build
```

- Gateway: http://localhost:8080

Остановить: `docker compose down`  
Полная очистка: `docker compose down -v`

## API (через gateway на localhost:8080)

### computer-store

| Метод | URL | Описание |
|---|---|---|
| POST | `/api/computers` | Принять компьютер |
| GET | `/api/computers` | Список (`?status=AVAILABLE/SOLD`) |
| GET | `/api/computers/{id}` | Получить по id |
| PUT | `/api/computers/{id}` | Редактировать |
| PATCH | `/api/computers/{id}/sell` | Продать |
| GET | `/api/computers/report` | Отчёт |

### report-service

| Метод | URL | Описание |
|---|---|---|
| GET | `/api/reports/computers` | Список всех (через RestTemplate) |
| GET | `/api/reports/available` | Только доступные |
| GET | `/api/reports/sold` | Только проданные |
| GET | `/api/reports/summary` | Сводка |

## Примеры запросов

### Создать компьютер

```http
POST http://localhost:8080/api/computers
{
  "brand": "Apple",
  "model": "MacBook Pro 14",
  "serialNumber": "0123456",
  "price": 1999.99
}
```

### Сводка отчёта (через RestTemplate)

```http
GET http://localhost:8080/api/reports/summary
```

Ответ:

```json
{
  "total": 2,
  "available": 1,
  "sold": 1
}
```

## Модель данных

Одна таблица — `computer`: `id`, `brand`, `model`, `serial_number`, `price`, `status` (AVAILABLE/SOLD), `created_at`, `updated_at`.

## Ключевые концепции

- **Data Ownership** — у данных один владелец (`computer-store`)
- **RestTemplate** — синхронный HTTP-клиент для межсервисных вызовов
- **DTO** — отдельный класс для передачи данных (без JPA-аннотаций)
- **Каскадный отказ** — падение `computer-store` ломает и `report-service`



## Особенности

- Только gateway проброшен наружу (порт 8080)
- Сервисы доступны внутри Docker-сети по DNS-именам
- Миграции Liquibase выполняет только `computer-store`
- report-service зависит от computer-store (каскадный отказ)
