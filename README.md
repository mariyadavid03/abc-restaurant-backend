# ABC Restaurant Web Application - Backend
This repository contains the Spring Boot-based backend API for the ABC Restaurant Web Application. It provides services for user authentication, order management, reservation handling, and report generation, interacting with the frontend to manage restaurant operations.

## Technologies Used
- Spring Boot: Framework used to build the backend API.
- PostgreSQL: Database used for storing data.
- JavaMailSender: For sending email notifications.
- JUnit: For unit testing the application.
- Selenium Web Driver - For end to end testings

## Installation
To get started with the backend locally, follow these steps:

1. Clone the repository - run, git clone https://github.com/mariyadavid03/abc-restaurant-backend.git. then run, cd abc-restaurant-backend
2. Set up PostgreSQL Database: Create a database in postres, then update the application.properties file with your MySQL credentials:

properties
spring.datasource.url=jdbc:mysql://localhost:3306/restaurant_db
spring.datasource.username=your-username
spring.datasource.password=your-password

3. Install dependencies and run the application - Ensure you have Maven and Java installed, then run, 
mvn install
mvn spring-boot:run

The backend API should now be running on http://localhost:8080.

## Key API Endpoints
### Authentication
- POST /signup: Register a new user.
- POST /login: Authenticate a user.
  
### Profile Management
- PUT /profile/update: Update user profile details.

### Orders & Deliveries
- GET /orders: View all orders.
- POST /orders: Place a new order.
- DELETE /orders/{id}: Cancel an order.

### Reservations
- GET /reservations: View all reservations.
- POST /reservations: Create a new reservation.
- DELETE /reservations/{id}: Cancel a reservation.

### Queries
- POST /query: Submit a query.
- GET /query: View customer queries (staff/admin).

### Reports
- GET /reports/payments: Generate a payment report based on date range.
- GET /reports/reservations: Generate a reservation report based on date range.

## Folder Structure
- src/main/java: Contains the Java source code for the backend API.
  - controller: Handles the API endpoints.
  - service: Contains the business logic.
  - repository: Handles database interactions.
  - model: Contains entity classes representing database tables.
- src/main/resources: Contains configuration files such as application.properties.
- src/test: Contains all the test files.
  
## Running Tests
To run the unit tests, use the following Maven command:
mvn test

## License
This project is licensed under the MIT License. See the LICENSE file for more details.
