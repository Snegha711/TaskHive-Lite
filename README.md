# TaskHive-Lite
"TaskHive Lite" is a task and project management system built with Java and Spring Boot. It allows users to manage tasks, projects, and user roles with JWT-based authentication. Features include task CRUD operations, project management, and user account management for effective collaboration.
Here's a simple and structured README template that you can use for your GitHub repository:

## Features

- **User Management**: Register, login, and update user details.
- **Project Management**: Create, update, and delete projects.
- **Task Management**: Create, update, assign, and delete tasks within projects.
- **JWT Authentication**: Secure endpoints with JWT tokens for user authentication.
- **Basic CRUD operations** for managing tasks, projects, and users.

## Tech Stack

- **Backend**: Java 8 / Spring Boot
- **Database**: H2 (for lightweight storage)
- **Security**: Spring Security with JWT for authentication
- **Persistence**: JPA (Hibernate)
- **API Documentation**: OpenAPI (Swagger)

## Endpoints

- **POST** `/api/auth/login`: Login and get JWT token.
- **POST** `/api/auth/register`: Register a new user.
- **GET** `/api/tasks`: Get all tasks.
- **POST** `/api/tasks`: Create a new task.
- **GET** `/api/tasks/{taskId}`: Get task by ID.
- **PUT** `/api/tasks/{taskId}`: Update task.
- **DELETE** `/api/tasks/{taskId}`: Delete task.
- **GET** `/api/projects`: Get all projects.
- **POST** `/api/projects`: Create a new project.
- **GET** `/api/projects/{projectId}`: Get project by ID.
- **PUT** `/api/projects/{projectId}`: Update project.
- **DELETE** `/api/projects/{projectId}`: Delete project.

## How to Run the Project

### Prerequisites

- **JDK 8+**: Ensure you have Java Development Kit 8 or higher installed.
- **Maven**: Make sure Maven is installed for building the project.

### Steps to Run

1. Clone this repository:
   ```bash
   git clone https://github.com/yourusername/TaskHive-Lite.git
   ```

2. Navigate to the project folder:
   ```bash
   cd TaskHive-Lite
   ```

3. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

4. The application will be running at `http://localhost:8086`.

## API Documentation

You can test the API using Postman or Swagger UI (if integrated). The API is protected by JWT authentication, so you’ll need to first log in and obtain a token.

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Commit your changes.
4. Push to your forked repository.
5. Submit a pull request.
