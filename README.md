Лабораторная работа №1. Учёт компьютеров в магазине

## Стек

- Java 17, Spring Boot 3.2.5, Gradle 8.5
- PostgreSQL 16, Liquibase
- springdoc-openapi (Swagger UI)
- Docker / Docker Compose

## Функционал

- Принять новый компьютер
- Оформить продажу
- Отредактировать информацию
- Отчёт: сколько имеется и сколько продано

## Запуск

```bash
docker compose up --build
```

- Приложение: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui.html

Остановить: `docker compose down`
Полная очистка: `docker compose down -v`

## API

| Метод | URL | Описание |
|---|---|---|
| POST | `/api/computers` | Принять компьютер |
| GET | `/api/computers` | Список (`?status=AVAILABLE/SOLD`) |
| GET | `/api/computers/{id}` | Получить по id |
| PUT | `/api/computers/{id}` | Редактировать |
| PATCH | `/api/computers/{id}/sell` | Продать |
| GET | `/api/computers/report` | Отчёт |

## Пример запроса

```http
POST /api/computers
{
  "brand": "Apple",
  "model": "MacBook Pro 14",
  "serialNumber": "SN123456",
  "price": 1999.99
}
```

## Модель данных

Одна таблица — `computer`: `id`, `brand`, `model`, `serial_number`, `price`, `status` (`AVAILABLE`/`SOLD`), `created_at`, `updated_at`.
