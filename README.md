# Лабораторная работа №2. Сервис отчётов + API Gateway

Микросервисная архитектура для учёта компьютеров в магазине.

## Стек

- Java 17, Spring Boot 3.2.5, Gradle 8.5
- Spring Cloud Gateway 2023.0.1
- PostgreSQL 16, Liquibase
- Docker / Docker Compose

## Архитектура

```
Postman → Gateway (8080) ──┬──> computer-store (8081) ──> PostgreSQL
                           └──> report-service (8082) ──> PostgreSQL
```

- **computer-store** — CRUD, владелец схемы, применяет миграции Liquibase
- **report-service** — только чтение, отчёты
- **gateway** — единая точка входа, маршрутизация
- **PostgreSQL** — общая БД

## Функционал

### computer-store
- Принять новый компьютер
- Оформить продажу
- Отредактировать информацию
- Отчёт: сколько имеется и сколько продано

### report-service
- Список всех компьютеров
- Только доступные
- Только проданные
- Сводка

## Запуск

```bash
docker compose up --build
```

- Gateway: http://localhost:8080
- Swagger computer-store: http://localhost:8081/swagger-ui.html (внутри сети)
- Swagger report-service: http://localhost:8082/swagger-ui.html (внутри сети)

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
| GET | `/api/reports/computers` | Список всех |
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

### Сводка отчёта

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

## Особенности

- Только gateway проброшен наружу (порт 8080)
- Сервисы доступны внутри Docker-сети по DNS-именам
- Миграции Liquibase выполняет только `computer-store`
- Частичный отказ: остановка report-service не ломает computer-store
