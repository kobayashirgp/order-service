# Order Service

This service aims to persist an order with its respective products to later carry out the processing through an automated job and make the result available in a query API in the REST standard.

## Stack
* **Java 21**
* **Spring Boot 3**
* **Spring Data**
* **Spring Batch**
* **Spring Web MVC**
* **Hibernate**
* **Swagger 3**
* **PostgreSQL**
* **Shedlock**

## Prerequisites
* **JDK 21**
* **Gradle**
* **Git**
* **Docker**
* **Docker compose**

## Running the project
After cloning the project, it is necessary to use Docker Compose to upload a local database. Use the following command
```
sudo docker compose up
```

## Swagger docs
http://localhost:8080/swagger-ui/index.html

## Payload Examples (cUrl)

* Get orders paginated
```
curl --location 'http://localhost:8080/orders?page=0&size=10'
```
* Save an order
```
curl --location 'http://localhost:8080/orders' \
--header 'Content-Type: application/json' \
--data '{
    "externalId": 4,
    "products": [
        {
            "description": "Product 1",
            "amount": 10
        },
        {
            "description": "Product 1",
            "amount": 15
        },
        {
            "description": "Product 1",
            "amount": 20
        }
    ]
}'
```