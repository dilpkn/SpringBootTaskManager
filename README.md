# 📌 SpringTacksManager

**SpringTacksManager** — это RESTful веб-приложение для управления задачами (Task Manager), разработанное на Spring Boot. Приложение предоставляет API для создания, просмотра, обновления и удаления задач, а также для изменения их статусов.

---

## 🚀 Стек технологий

| Компонент         | Технология                   |
|-------------------|------------------------------|
| **Backend**       | Java 17, Spring Boot 4.0.2   |
| **База данных**   | PostgreSQL 18                |
| **ORM**           | Spring Data JPA (Hibernate)  |
| **Сборка**        | Maven                        |
| **API**           | RESTful (JSON)               |

---

## 📁 Структура проекта

```
org.example.springtacksmanager/
├── SpringTacksManagerApplication.java # Точка входа
├── GlobalExceptionHandler.java # Глобальная обработка ошибок
├── ErrorResponseDto.java # DTO для ошибок
│
├── task/ # Основной пакет с логикой задач
│ ├── Task.java # Domain-модель (record)
│ ├── TaskEntity.java # JPA-сущность
│ ├── TaskController.java # REST-контроллер
│ ├── TaskService.java # Бизнес-логика
│ ├── TaskRepository.java # Репозиторий (JPA)
│ ├── TaskMapper.java # Маппер Entity ↔ Domain
│ ├── TaskFilter.java # Фильтр для поиска
│ ├── StatusEnum.java # Статусы задачи
│ └── PriorityEnum.java # Приоритеты задачи
│
└── resources/
└── application.properties # Конфигурация приложения
```
---

## ⚙️ Конфигурация

### Файл `application.properties`

```properties
spring.application.name=SpringTacksManager

# Подключение к БД (PostgreSQL на порту 5556)
spring.datasource.url=jdbc:postgresql://localhost:5556/postgres
spring.datasource.username=postgres
spring.datasource.password=root

# JPA настройки
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 📦 Модель данных
Task (domain-модель)
```
record Task(
    Long id,
    Long creatorId,
    Long assignedUserId,
    StatusEnum statusEnum,
    LocalDateTime startDate,
    LocalDateTime endDate,
    PriorityEnum priorityEnum,
    LocalDateTime doneDateTime
)
```

### TaskEntity (JPA-сущность)
```
Поле	|            Тип в БД |     	Описание
id|              	BIGINT (PK)	|      Уникальный идентификатор
creator_id|      	BIGINT|	            ID создателя
assigned_user_id|	BIGINT	|          ID исполнителя
status|          	VARCHAR	|          Статус (CREATED, IN_PROGRESS, DONE)
start_date|     	TIMESTAMP|	        Дата начала
end_date|        	TIMESTAMP|	        Дата окончания
priority|        	VARCHAR	|            Приоритет (LOW, MEDIUM, HIGH)
done_date_time|  	TIMESTAMP|    	    Дата завершения
```

### 🌐 REST API
```
📍 Базовый URL: /tasks
Метод	Endpoint	Описание	Коды ответа
GET	/tasks	Получить все задачи	200 OK
GET	/tasks/{id}	Получить задачу по ID	200 OK / 404 Not Found
POST	/tasks	Создать новую задачу	201 Created / 400 Bad Request
PUT	/tasks/{id}	Обновить задачу	200 OK / 400 / 404
DELETE	/tasks/{id}	Удалить задачу (только в статусе CREATED)	204 No Content / 400 / 404
POST	/tasks/{id}/start	Запустить задачу (перевести в IN_PROGRESS)	200 OK / 400 / 404
POST	/tasks/{id}/complete	Завершить задачу	200 OK / 400 / 404
GET	/tasks/search	Поиск с фильтрацией и пагинацией	200 OK
```

### 🔍 Поиск с фильтрацией
Endpoint: GET /tasks/search

Параметры запроса (все опциональны):
```
Параметр	Тип	Описание
creatorId	Long	ID создателя
assignedUserId	Long	ID исполнителя
status	StatusEnum	Статус задачи
priority	PriorityEnum	Приоритет задачи
pageSize	Integer	Размер страницы (по умолчанию 5)
pageNumber	Integer	Номер страницы (по умолчанию 0)
```

Пример запроса:
```
GET /tasks/search?status=IN_PROGRESS&priority=HIGH&pageSize=10&pageNumber=0
```
### 📥 Примеры запросов
🔹 Создание задачи

Request:
```
POST /tasks
Content-Type: application/json

{
    "creatorId": 1,
    "assignedUserId": 2,
    "startDate": "2026-03-01T10:00:00",
    "endDate": "2026-03-10T18:00:00",
    "priorityEnum": "HIGH"
}
```
Response (201 Created):
```
{
    "id": 42,
    "creatorId": 1,
    "assignedUserId": 2,
    "statusEnum": "CREATED",
    "startDate": "2026-03-01T10:00:00",
    "endDate": "2026-03-10T18:00:00",
    "priorityEnum": "HIGH",
    "doneDateTime": null
}
```
#### 🔹 Запуск задачи
POST /tasks/42/start
#### 🔹 Завершение задачи
POST /tasks/42/complete
### ❗ Обработка ошибок

Глобальный обработчик GlobalExceptionHandler возвращает ошибки в едином формате:
```
{
    "message": "internal server error",
    "detailedMessage": "Task status should be CREATED",
    "timeError": "2026-02-28T13:42:54.418"
}
```
HTTP статус	Тип ошибки
400 Bad Request	IllegalStateException, IllegalArgumentException
404 Not Found	EntityNotFoundException
500 Internal Server Error	Общие исключения

### 🗄️ База данных
Схема таблицы tasks
```
CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    creator_id BIGINT,
    assigned_user_id BIGINT,
    status VARCHAR(255) CHECK (status IN ('CREATED', 'IN_PROGRESS', 'DONE')),
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    priority VARCHAR(255) CHECK (priority IN ('LOW', 'MEDIUM', 'HIGH')),
    done_date_time TIMESTAMP
);
```

### 🚀 Запуск проекта
```
1️⃣ Запустить PostgreSQL
docker run --name postgres-tasks -e POSTGRES_PASSWORD=root -p 5556:5432 -d postgres
2️⃣ Собрать проект
mvn clean package
3️⃣ Запустить приложение
mvn spring-boot:run
4️⃣ Приложение доступно по адресу
http://localhost:8080
```
