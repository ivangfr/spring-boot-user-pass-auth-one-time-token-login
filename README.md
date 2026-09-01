# spring-boot-user-pass-auth-one-time-token-login

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Buy Me A Coffee](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-ivan.franchin-FFDD00?logo=buymeacoffee&logoColor=black)](https://buymeacoffee.com/ivan.franchin)

The goal of this project is to create a [`Spring Boot`](https://docs.spring.io/spring-boot/index.html) application called `movies-app` that allows users to log in using [`Username/Password Authentication`](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/index.html) and [`One-Time Token Login`](https://docs.spring.io/spring-security/reference/servlet/authentication/onetimetoken.html).

## Proof-of-Concepts & Articles

On [ivangfr.github.io](https://ivangfr.github.io), I have compiled my Proof-of-Concepts (PoCs) and articles. You can easily search for the technology you are interested in by using the filter. Who knows, perhaps I have already implemented a PoC or written an article about what you are looking for.

## Additional Readings

- \[**Medium**\] [**Spring Boot App with Username/Password Authentication and One-Time Token Login**](https://medium.com/@ivangfr/spring-boot-app-with-username-password-authentication-and-one-time-token-login-fe3da92f0cb0)
- \[**Medium**\] [**Spring Security One-Time Token Login Explained**](https://medium.com/@ivangfr/spring-security-one-time-token-login-explained-110a8da53c90)

## Project Overview

```mermaid
flowchart LR
    subgraph users ["Users"]
        Browser["Browser"]
    end

    subgraph movies-app ["movies-app\n(Spring Boot)"]
        RestCtrl["MoviesAppController\n/ (index)\n/register\n/check-email\n/movies\n/users"]
        Security["SecurityConfig\n(formLogin + oneTimeTokenLogin)"]
        EmailService["EmailService\n(JavaMailSender)"]
        UserRepository["UserRepository\n(JPA)"]
    end

    subgraph postgres ["PostgreSQL"]
        db[("userdb\nusers table")]
    end

    subgraph mailpit ["MailPit"]
        SMTPServer["SMTP Server\n(port 1025)"]
        WebUI["MailPit Web UI\n(http://localhost:8025)"]
    end

    Browser -->|"HTTP\n(GET/POST)"| RestCtrl
    RestCtrl -->|"queries"| UserRepository
    RestCtrl -->|"validates credentials"| Security
    Security -->|"authenticates"| UserRepository
    UserRepository -->|"JDBC queries"| db
    Security -->|"generates OTT"| EmailService
    EmailService -->|"sends email\n(SMTP)"| SMTPServer
    SMTPServer -->|"captures emails"| WebUI
    Browser -->|"views emails"| WebUI
```

## Applications

- ### movies-app

  A `Spring Boot` Java web application that provides a user interface for accessing movie information.
  
  During startup, an admin for the Movies App is created with the _username_ `admin` and the _password_ `admin`.

  Users must register by providing a _username_, _password_, and _email_.

  Once registered, both admins and users can log in either with their _username_ and _password_ or by requesting a one-time token sent to their _email_.

- ### MailPit

  The application uses [`MailPit`](https://mailpit.axllent.org/). It's a lightweight email testing tool that captures and displays emails from your application in a web interface. It helps developers test email functionality without sending real emails.

## Prerequisites

- [`Java 25`](https://www.oracle.com/java/technologies/downloads/#java25) or higher;
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
- To stop and remove Docker Compose containers, network, and volumes, go to a terminal and, inside `spring-boot-user-pass-auth-one-time-token-login` root folder, run the command below:
  ```bash
  docker compose down -v
  ```

## Running Tests

In a terminal, make sure you are inside the `spring-boot-user-pass-auth-one-time-token-login` root folder, and run the following command:
```bash
./mvnw clean test --projects movies-app
```

## Code Formatting

This project enforces consistent Java formatting using the [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-maven) Maven plugin with [google-java-format](https://github.com/google/google-java-format) (GOOGLE style).

- **Check formatting**:
  ```bash
  ./mvnw spotless:check
  ```

- **Auto-fix formatting**:
  ```bash
  ./mvnw spotless:apply
  ```

Formatting is enforced automatically during `./mvnw test`.

## How to optimize the GIF in the documentation folder

\[**Medium**\]: [**How I Reduce GIF and Screenshot Sizes for My Technical Articles on macOS**](https://medium.com/itnext/how-i-reduce-gif-and-screenshot-sizes-for-my-technical-articles-on-macos-7fea331afc68)

## Support

If you find this useful, consider buying me a coffee:

<a href="https://buymeacoffee.com/ivan.franchin"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" height="50"></a>

## License

This project is licensed under the [MIT License](./LICENSE).
