# ClickTalk Backend

## Overview

ClickTalk is a backend service for a messaging application. It provides APIs for user authentication, conversation management, and message handling.

## Features

- User authentication and authorization using JWT.
- CRUD operations for users, conversations, projects, and messages.
- MySQL database integration.
- API documentation with Swagger.
- Basic exception handling and global error handling.
- User settings and themes support.
- Security configurations with Spring Security.
- Environment variable support for configuration.

## Getting Started

### Prerequisites

- Java 11 or higher
- Maven
- MySQL

### Installation

1. Clone the repository:

   ```sh
   git clone https://github.com/yourusername/ClickTalk-backend.git
   cd ClickTalk-backend
   ```

2. Set up the MySQL database:

   ```sql
   CREATE DATABASE clicktalk;
   ```

3. Create a .env file in the root directory and add your environment variables:

   ```properties
    DB_URL=jdbc:mysql://localhost:3306/clicktalk_db
    DB_USERNAME=yourusername
    DB_PASSWORD=yourpassword

    JWT_SECRET=yourjwtsecret
    JWT_EXPIRATION=yourjwtecpiration

    API_URL=https://api.deepseek.com/chat/completions
    API_KEY=yourapikey
   ```

4. Build the project:

```sh
mvn clean install
```

5. Run the application:
   ```sh
   mvn spring-boot:run
   ```

### API Documentation

Access the API documentation at `http://localhost:8080/swagger-ui.html`.

## Contributing

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
