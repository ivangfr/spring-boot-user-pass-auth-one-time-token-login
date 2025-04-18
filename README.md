# spring-boot-user-pass-auth-one-time-token-login

The goal of this project is to create a [`Spring Boot`](https://docs.spring.io/spring-boot/index.html) application called `movies-app` that allows users to log in using [`Username/Password Authentication`](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html) and [`One-Time Token Login`](https://docs.spring.io/spring-security/reference/servlet/authentication/onetimetoken.html).

## Proof-of-Concepts & Articles

On [ivangfr.github.io](https://ivangfr.github.io), I have compiled my Proof-of-Concepts (PoCs) and articles. You can easily search for the technology you are interested in by using the filter. Who knows, perhaps I have already implemented a PoC or written an article about what you are looking for.

## Additional Readings

- \[**Medium**\] [**Spring Boot App with Username/Password Authentication and One-Time Token Login**](https://medium.com/@ivangfr/spring-boot-app-with-username-password-authentication-and-one-time-token-login-fe3da92f0cb0)

## Applications

- ### movies-app

  A `Spring Boot` Java web application that provides a user interface for accessing movie information.
  
  During startup, an admin for the Movies App is created with the _username_ `admin` and the _password_ `admin`.

  Users must register by providing a _username_, _password_, and _email_.

  Once registered, both admin and users can log in either with their _username_ and _password_ or by requesting a one-time token sent to their _email_.

- ### MailPit

  We are using [`MailPit`](https://mailpit.axllent.org/). It's a lightweight email testing tool that captures and displays emails from your application in a web interface. It helps developers test email functionality without sending real emails.

## Prerequisites

- [`Java 21`](https://www.oracle.com/java/technologies/downloads/#java21) or higher;
- A containerization tool (e.g., [`Docker`](https://www.docker.com), [`Podman`](https://podman.io), etc.)

## Start Environment

- In a terminal, navigate to `spring-boot-user-pass-auth-one-time-token-login` root folder;
- Run the following command to start Docker Compose containers:
  ```bash
  docker compose up -d
  ```

## Running movies-app using Maven

- In a terminal, make sure you are in `spring-boot-user-pass-auth-one-time-token-login` folder;
- Run the following `Maven` command to start the application:
  ```bash
  ./mvnw clean spring-boot:run --projects movies-app
  ```

## Application URLs

| Application  | URL                   |
|--------------|-----------------------|
| `movies-app` | http://localhost:8080 |
| `MailPit`    | http://localhost:8025 |

## Demonstration

- ### User Registration

  ![user-registration](documentation/user-registration.gif)

- ### Username/Password Login

  ![username-password-login](documentation/username-password-login.gif)

- ### One-Time Token Login

  ![one-time-token-login](documentation/one-time-token-login.gif)

  > **Note**: If the admin or user logs out and tries to log in again using the same token, it will not work!

## Util Commands

- **Postgres**
  ```bash
  docker exec -it postgres psql -U postgres -d userdb
  \dt
  select * from users;
  ```

## Shutdown

- To stop `movies-app`, go to the terminal where it's running and press `Ctrl+C`;
- To stop and remove Docker Compose containers, network and volumes, go to a terminal and, inside `spring-boot-user-pass-auth-one-time-token-login` root folder, run the command below:
  ```bash
  docker compose down -v
  ```
