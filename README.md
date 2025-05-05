update_ci.yml
# Online Book Store
***
This service is designed to make it easy and convenient for users to search and buy books.

## Project Overview
***
This project is a bookstore management system. Users can select a book according to their interests, search by title, sort by category, and add the selected book to the cart.
Access rights in system management are divided into user(client) and administrator, only the administrator can make changes such as update or delete in the database.
More details about this in the description of each endpoint

## Technologies
***
The project is based on the Spring Framework and a set of technologies for creating a secure and scalable application.
### Technologies used:
* **Spring Boot**
* **Spring Security**
* **Spring Web**
*  **Spring Data JPA**
*  **JWT (JSON Web Tokens)**
*  **Lombok**
*  **MapStruct**
*  **Swagger**
*  **MySQL**
*  **Liquibase**
*  **Docker**


## API using instructions
***
1. Clone this API
```
git clone https://github.com/slava-moiseev-kramatorsk/spring_boot_intro
```
2. Set up the database

Make sure the MySQL is installed.

Create a ```.env``` file and configure it
```
MYSQLDB_ROOT_PASSWORD=your_password
MYSQLDB_DATABASE=your_database_name
MYSQLDB_USER=your_user_name
MYSQLDB_LOCAL_PORT=3307
MYSQLDB_DOCKER_PORT=3306
SPRING_LOCAL_PORT=8088
SPRING_DOCKER_PORT=8080
DEBUG_PORT=5005
```

For ease of use, a user with the administrator role was added.  Username: "admin@gmail.com", password: "12345"

3. Access Swagger UI

For API description and usage, open the link in your browser
```http://localhost:8088/swagger-ui/```

## API Endpoints
***

Access to the API is divided depending on the user's role. Two levels of access are possible:
* **Administrator**
* **Client**

Registration and authorization are required to use protected endpoints. 
First, you need to go through the registration process to obtain an access token (JWT), which will be used to access protected resources.

### Authentication

* **POST: /api/auth/register (To register a new user)**

Example request body:
```json
{
  "email": "Anderson@example.com",
  "password": "prettyWoman",
  "repeatPassword": "prettyWoman",
  "firstName": "Andy",
  "lastName": "Anderson",
  "shippingAddress": "your address"
}
```

Example response body :
```json 
{
  "id": 1,
  "email": "Anderson@example.com",
  "firstName": "Andy",
  "lastName": "Anderson",
  "shippingAddress": "your address"
}
```
* **POST: /api/auth/login**

Example request body:
```json
{
  "email": "Anderson@example.com",
  "password": "prettyWoman"
}
```

Example response body:
```json
{
  "token": "your_jwt_token"
}
```

### Book

Get all books
* **GET: /api/books**

Example response body:

```json
[
  {
    "id": 1,
    "title": "The dark tower",
    "author": "S. King",
    "price": 19.19,
    "category": "Fantasy",
    "description": "Western, magic, adventure, journey",
    "categoryIds": [1]
  }
]
```

Get book by id
* **GET: /api/books{id}**

Example response body:

```json
[
  {
    "id": 14,
    "title": "I Am Legend",
    "author": " Richard Burton Matheson",
    "price": 9.95,
    "category": "Horror",
    "description": "loneliness, survival",
    "categoryIds": [1]
  }
]
```

Create a new book.

This API is only available to administrators. Before creating, make sure that the corresponding category exists in the database.

* **POST: /api/books**

Examole request body:
```json
{
    "title": "Stand",
    "author": "S.King",
    "isbn": "98-343434534",
    "price": 15.96,
    "description": "Fantasy",
    "coverImage": "exampleImage.png",
    "categoriesIds": [2]
}
```

Example response body:
```json
[
  {
    "id": 15,
    "title": "Stand",
    "author": "S.King",
    "isbn": "98-343434534",
    "price": 15.96,
    "description": "Fantasy",
    "coverImage": "exampleImage.png",
    "categoryIds": [2]
  }
]
```

Update book.

This API is only available to administrators.

* **PUT: /api/books{id}**

Examole request body:
```json
{
    "title": "Stand",
    "author": "S.King",
    "isbn": "0001111",
    "price": 20.00,
    "description": "Fantasy",
    "coverImage": "Poster.png",
    "categoriesIds": [2]
}
```

Example response body:
```json
[
  {
    "title": "Stand",
    "author": "S.King",
    "isbn": "0001111",
    "price": 20.00,
    "description": "Fantasy",
    "coverImage": "Poster.png",
    "categoriesIds": [2]
  }
]
```

Delete book. Only administrator has access

* **DELETE: /api/books/{id}**

Example response body:
* *Status code: ```204 No content```

### Category

Get all categories

* **GET: /api/categories**

Example response body:
```json
[
  {
    "id": 1,
    "name": "Horror",
    "description": "Horror books, scary stories"
  },
  {
    "id": 2,
    "name": "Fantasy",
    "description": "Books about fantastic world, adventures, journey"
  }
]
```

Create a new category. Only administrator has access

* **POST: /api/categories**

Example request body:
```json
{
  "name": "Education",
  "description": "Books for learning"
}
```

Example response body:
```json
{
  "id": 3,
  "name": "Education",
  "description": "Books for learning"
}
```

Find all books by category id

* **GET: /api/categories/{id}/books

Example response body:
```json
[
  {
    "id": 1,
    "title": "Stand",
    "author": "S.King",
    "isbn": "98-343434534",
    "price": 15.96,
    "description": "Fantasy",
    "coverImage": "exampleImage.png",
    "categoriesIds": [2]
  },
  {
    "id": 3,
    "title": "The dark tower",
    "author": "S. King",
    "price": 19.19,
    "category": "Fantasy",
    "description": "Western, magic, adventure, journey",
    "categoryIds": [2]
  }
]
```

Update category by id

* **PUT: /api/categories/{id}**

Example request body:
```json
{
  "name": "Updated name",
  "description": "Updated description"
}
```

Example response body:
```json
{
  "id": 3,
  "name": "Updated name",
  "description": "Updated description"
}
```

Delete category

* **DELETE: /api/categories/{id}**

Example response body:
* *Status code: ```204 No content```

### Order

Get orders of the current authorized user

* **GET: /api/orders**

Example response body
```json
[
  {
    "id": 1,
    "userId": 1,
    "orderItems": [
      {
        "id": 1,
        "bookId": 1,
        "quantity": 1
      },
      {
        "id": 2,
        "bookId": 2,
        "quantity": 2
      }
    ],
    "orderDate": "2025-05-03T16:44:05",
    "total": 39.19,
    "status": "PENDING"
  }
]
```

Get order items by order id

* **GET /api/orders/{orderId}/items**

Example response body:
```json[
  {
    "id": 1,
    "bookId": 1,
    "quantity": 1
  },
  {
    "id": 2,
    "bookId": 2,
    "quantity": 1
  }
]
```

Create a new order

* **POST /api/orders**

Example request body:
```
[
  {
  "shippingAddress": "your adress"
  }
]
```
Example response body:
```
[
  {
  "id": 1,
  "userId": 1,
  "orderItems": [
    {
      "id": 1,
      "bookId": 1,
      "quantity": 1
    },
    {
      "id": 2,
      "bookId": 2,
      "quantity": 1
    }
  ],
  "orderDate": "2025-05-03T16:44:05",
  "total": 39.19,
  "status": "PENDING"
  }
]
```

Update order status

* **PATCH: /api/orders/{id}**

Example response body:
```json
[
  {
  "status": "COMPLETED"
  }
]
```

Example response body:
```json
[
  {
  "id": 1,
  "userId": 1,
  "orderItems": [
    {
      "id": 1,
      "bookId": 1,
      "quantity": 1
    },
    {
      "id": 2,
      "bookId": 2,
      "quantity": 1
    }
  ],
  "orderDate": "2025-05-03T16:44:05",
  "total": 39.19,
  "status": "COMPLETED"
  }
]
```

### Shopping Cart

Get shopping cart

* **GET: /api/cart**

Example response body:
```
[
  {
  "id": 1,
  "userId": 1,
  "cartItemsIds": [1, 2]
  }
]
```

Add book to shopping cart

* **POST: /api/cart**

Example request body:
```json
[
  {
  "bookId": 3,
  "quantity": 1
  }
]
```

Example response body:
```json
[
  {
  "id": 1,
  "userId": 1,
  "cartItemsIds": [1, 2, 3]
  }
]
```

Update cart item by id

* **PATH: /api/cart/items/{cartItemId}**

Example request body:
```json
[
  {
  "quantity": 2
  }
]
```

Example response body:
```json
[
  {
  "bookId": 3,
  "quantity": 3
  }
]
```

Delete cart item by id

* **DELETE: /api/cart/items/{cartItemId}**

Example response body:
* *Status code: ```204 No content```