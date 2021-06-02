# academia-java-api
A project to teach REST APIs with Java and Spring Boot

# The Academy
We are going to build a Customer REST API in order to provide a CRUD (Create, Read, Update, Delete) to manage Customer's data. 

There will be a Swagger Proof provided in the base project.

# The Challenge
The base project has an error. There will be a prize to those who identify, solve and send a PR (Pull Reques) to the base project.

# Concepts
- What is a REST API?
Is an application that uses the HTTP protocol to communicate and expose system data.

- Why should we use a REST API?
In order to provide communication and integration between systems.

- How can we implement a REST API?
There are many ways to do this. In Santander we have been using Java and Spring Boot framework. 

# Session 1
- [JDK 11](https://jdk.java.net/archive/)
- [IntelliJ Community](https://www.jetbrains.com/idea/download/#section=windows)
- [Lombok Plugin](https://projectlombok.org/setup/intellij)
- [Maven](https://mirrors.up.pt/pub/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.zip)
- [Postman](https://www.postman.com/downloads/)
- Create Github account and project
- Clone Github project
- [SpringBoot Initializer](https://start.spring.io/)
- IntelliJ: Setup SDK
- Change IDE Java: Help > Find Action > Switch Boot JDK
- Add pom.xml as Maven project
- Run application
- First commit

# Session 2 
- Create SQL objects
- Setup in-memory database
- Setup REST API
- Add as Maven project
- Hello world controller
- Understanding the Swagger contract
:warning Deactivate CORS in Chrome. Stop all running chrome.exe and then in a terminal:
```
Right click on desktop, add new shortcut.
Add the target as "[PATH_TO_CHROME]\chrome.exe" --disable-web-security --disable-gpu --user-data-dir=~/chromeTemp.
Click OK.
```

# Session 3
- Implement GET /customers controller
- The DTO pattern
- The service interface and its implementation
- The repository and its entities
- The ORM mapping
- Create Postman collection

# Session 4
- Implement POST /customers controller 
-- @Email
-- Why dto to entity and vice-versa? Answer: encapsulation, dynamic responses, etc.
- Implement PUT /customers controller
- Implement DELETE /customers controller
- Explore Query native and JPA
- A conversation about Spring Security

# Session 5
- JUnit test
- Project Build with Maven
- Git feature, merge, etc..

# Session 6
- The Challenge

# References

## HTTP methods
https://developer.mozilla.org/en-US/docs/Web/HTTP/Methods

## HTTP status
https://en.wikipedia.org/wiki/List_of_HTTP_status_codes

## JPA X ORM X HIBERNATE
https://stackoverflow.com/questions/27462185/jpa-vs-orm-vs-hibernate

## Repository types
https://www.baeldung.com/spring-data-repositories

## Repository query
https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation





 