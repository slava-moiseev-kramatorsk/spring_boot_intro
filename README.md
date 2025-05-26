# Online Book Store
***
This service is designed to make it easy and convenient for users to search and buy books.

## Project Overview
***
This project is a bookstore management system. Users can select a book according to their interests, search by title, sort by category, and add the selected book to the cart.
Access rights in system management are divided into user(client) and administrator, only the administrator can make changes such as update or delete in the database.
This application is built on the following domain models (entities):
 * **User**: Represents a user in the system, including their personal information nd associated with a single role that defines their access rights
 * **Role**: Represents the role of a user in the system (Admin, Client)
 * **Book**: Represents a book available in the store
 * **Category**: Represents a category that a book can belong to
 * **ShoppingCart**: Represents a user's shopping cart
 * **CartItem**: Represents an item in a user's shopping cart
 * **Order**: Represents an item in a user's order.


### Models and Relationships

* **User**
  - id, email, password, firstName, lastName, shippingAddress
  - 📎 has one → ShoppingCart
  - 📎 has one → Order
  - 📎 has many → Roles
* **Role**
  - id, RoleName
  - 📎 many-to-many ← Users
* **Book**
  - id, title, author, isbn, price, description, coverImage
  - 📎 many-to-many ↔ Categories
  - 📎 referenced by → OrderItems
  - 📎 referenced by → CartItems
* **Category**
  - id, name, description
  - 📎 many-to-many ↔ Books
* **ShoppingCart**
  - id
  - 📎 belongs to → User
  - 📎 has many → CartItems
* **CartItem**
  - id, quantity
  - 📎 belongs to → ShoppingCart
  - 📎 references → Book
* **Order**
  - id, status, orderDate, total, shippingAddress
  - 📎 belongs to → User
  - 📎 has many → OrderItems
* **OrderItem**
  - id, quantity, price
  - 📎 belongs to → Order
  - 📎 references → Book

## Technologies
***
The project is based on the Spring Framework and a set of technologies for creating a secure and scalable application.
### Technologies used:
* **Spring Boot v.3.3.0**
* **Spring Security v.6.3.0**
* **Spring Web v.6.1.8**
*  **Spring Data JPA v.3.3.0**
*  **JWT (JSON Web Tokens) v.0.12.6**
*  **Lombok v.1.18.32**
*  **MapStruct v.1.5.5.Final**
*  **Swagger v.2.3.0**
*  **MySQL v.8.0.33**
*  **Liquibase v.4.27.0**
*  **Docker v.27.5.1**


## ENDPOINTS
***
### Available for all users:
***
* POST: /api/auth/register (To register a new user)
* POST: /api/auth/login (To get JWT tokens)

### Available for registered users
***
* GET: /api/books (Upload all  books)
* GET: /api/books/{id} (Find book by id)
* GET: /api/categories (Get all categories)
* GET: /api/categories/{id} (Find category by id)
* GET: /api/categories/{id}/books (Get books by category)
* POST: /api/orders (To place an order)
* GET: /api/orders/{orderId}/items (Get order items)
* GET: /api/orders/{orderId}/items/{itemId} (Get order item by id)
* GET: /api/cart (Get a cart)
* POST: /api/cart (To add a book to the cart)
* PUT: /api/cart/cart-items/{id} (Update order)
* DELETE: /api/cart/cart-items/{id} (Delete order)

### Available for admin users
***
* POST: /api/books (Save a new book to DB)
* DELETE: /api/books/{id} (Remove a book by id)
* PUT: /api/books/{id} (Update current book)
* POST: /api/categories (Create a new category)
* PUT: /api/categories/{id} (Update category)
* DELETE: /api/categories/{id} (Delete category)
* PATCH: /api/orders/{id} (To updates order status)


## API using instructions
***
Before you begin, make sure you have installed:
 * Java (JDK version 17 or higher)
 * Maven
 * MySQL
 * Docker Desktop
 * Git

1. Clone this API
```
git clone https://github.com/slava-moiseev-kramatorsk/spring_boot_intro
cd your_repo_name
```
2. Set up the database

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
3. Build and run the application
```
mvn clean package
docker-compose up --build
docker-compose down
```
For ease of use, a user with the administrator role was added.  Username: "admin@gmail.com", password: "12345"

4. Access Swagger UI

For API description and usage, open the link in your browser
```http://localhost:8088/swagger-ui/```

5. API Testing

Import the provided [Postman Collection](https://.postman.co/workspace/My-Workspace~0750f7c0-aec3-4052-85bf-d5e8b8680438/collection/36728804-8c10d1f4-94df-4951-8900-67b6cc2935d3?action=share&creator=36728804) to test API endpoints.


