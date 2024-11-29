# Direct Message Backend

Welcome to **Direct Message**, a real-time chat application built using Spring Boot, PostgreSQL, and WebSockets. This backend provides robust features for messaging, user management, and media handling, with secure authentication and cloud-based storage.

---

## Features

### Authentication
- JWT-Based Authentication: Ensures secure user sessions.
- Email Verification: Verifies user emails during registration.
- Two-Factor Authentication: Integrates Google Authenticator for additional security.

### Messaging Features
- Send Messages: Exchange text and media messages in real-time.
- Edit/Delete Messages: Modify or remove sent messages.
- Reactions: React to messages with emojis.
- Typing Indicators: See when someone is typing.
- Read Notifications: Notify users when their messages are read.
- Message History: View past messages with powerful search capabilities.

### Media Handling
- Media Messages: Send and receive images, videos, or other file types.
- AWS S3 Integration: Media files are securely stored in Amazon S3.

### User Management
- Edit Profile: Update user details and profile picture.
- Change Status: Set a custom user status.
- Friend Requests: Send, accept, or reject friend requests.

---

---

## Additional Features

### API Documentation
- **OpenAPI (Swagger) Docs**: Explore and interact with the API using Swagger UI.  
  The Swagger documentation is automatically generated and accessible at:
    ```
      http://<HOST>:<PORT>/swagger-ui.html
    ```
  
### Logging
- **Logs**: All application logs, including error and debug logs, are automatically generated and stored in the `/logs` directory.  
  These logs help with monitoring, debugging, and auditing application activity.


### Database Auditing
- **Auditing for All Entities**: The application tracks creation and update metadata for all database entities:
- **Created By**: The user who created the entity.
- **Updated By**: The user who last updated the entity.
- **Created At**: The timestamp when the entity was created.
- **Updated At**: The timestamp when the entity was last updated.

---


## Prerequisites

### Tools and Services Required
- **Docker**: To run the PostgreSQL database using the provided Docker Compose file.
- **AWS S3**: For storing media files.
- **Google Authenticator**: For 2FA.
- **SMTP Email Server**: For email verification.
- **Java 23**: Required to build and run the Spring Boot application.

## How to Run

### 1. Set Up Configuration Values
Ensure that you have filled in all the necessary configuration values in the `application.yml` file, including:
- Database connection details
  ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://<DB_HOST>:<DB_PORT>/<DB_NAME>
       username: <DB_USERNAME>
       password: <DB_PASSWORD>
- SMTP mail server settings
  ```yaml
     mail:
      host: <MAIL_HOST>
      port: <MAIL_PORT>
      username: <MAIL_USERNAME>
      password: <MAIL_PASSWORD>
  ```
    - If you don't have an SMTP server and need a local solution for email testing, you can use Maildev. It's an easy-to-use SMTP server that captures sent emails for debugging purposes.
      1. **Install Maildev**: You can install Maildev via Docker. If you don't have Docker installed, follow the Docker installation guide.

      2. **Run the following command to pull and start Maildev**:
         ```bash
         docker run -d -p 1080:1080 -p 1025:1025 maildev/maildev
          ```
- JWT secret key and expiration times
   ```yaml
    jwt:
      secretkey: <JWT_SECRET_KEY>
      expiration: <JWT_EXPIRATION_TIME>
      refreshExpiration: <JWT_REFRESH_EXPIRATION_TIME>

- AWS S3 settings
  ```yaml
    aws:
      s3:
        bucketName: <S3_BUCKET_NAME>
        region: <S3_REGION>
        accessKeyId: <AWS_ACCESS_KEY_ID>
        secretAccessKey: <AWS_SECRET_ACCESS_KEY>
- General configuration
    ```yaml
    general:
      messages:
        delete-minutes: <DELETE_WINDOW_IN_MINUTES>  
        edit-minutes: <EDIT_WINDOW_IN_MINUTES> 
      users:
        admin:
          email: <ADMIN_EMAIL>
          password: <ADMIN_PASSWORD>
        status:
          inactive-threshold-minutes: <INACTIVE_WINDOW>
        confirmation-token:
          expiration-minutes: <CONFIRMATION_TOKEN_EXPIRATION_TIME>
  
### 2. Run PostgreSQL with Docker Compose
The project includes a `docker-compose.yml` file that can be used to easily run a PostgreSQL instance. To do so, follow these steps:
1. Ensure Docker is installed on your machine.
2. In the root directory of the project, run the following command to start the PostgreSQL container:
  ```bash
 docker-compose up
  ```
### 3. Build and Run the Spring Boot Application
To build and run the backend server using Gradle:
1. Clone the repository to your local machine.
  ```bash
     git clone https://github.com/Gemois/directmessage.git
  ```
2. Navigate to the project directory and run the following command:
  ```bash
  ./gradlew bootRun
  ```
3. The application will start and be accessible on the port configured in application.yml.



