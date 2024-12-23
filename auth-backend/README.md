# AUTH-Backend

This project uses Spring Boot version 3.4.0.
Install Java 17 and Maven before starting.

## Database Setup

Before starting the application, you must create a local database in PostgreSQL. Follow these steps:

1. **Install PostgreSQL**: If PostgreSQL is not installed, download and install it from https://www.postgresql.org/download/.

2. **Database**
   Make sure PostgreSQL is running, and the databases `db_auth` is created.


- There are initializer scripts to create the tables and relations and add some data

3. **Configure Database Credentials**

Open `src/main/resources/application.properties` and update the database credentials as follows:

spring.datasource.url=jdbc:postgresql://localhost:5432/database_name

spring.datasource.username=<username>

spring.datasource.password=<password>

Replace `database_name` with the actual PostgreSQL database name.

Replace `username` with the actual PostgreSQL username.

Replace `password` with the actual PostgreSQL password.


## Database Migrations

This project uses Flyway to manage database migrations. To execute the migrations, simply start the application with the command `mvn spring-boot:run`. Flyway will automatically apply the migrations defined in the folder src/main/resources/db/migration.


### **Steps to Use the Batch Script**:

1. **Navigate to the project root**: Open a terminal and navigate to the root directory of your project.

- **Authentication service (auth)**: [http://localhost:8081/](http://localhost:8081/)

## Documentation with Swagger
### Swagger
This service's API is documented using Swagger, you can access the documentation at:

- **Swagger API for `auth`**: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)

## Documentation with Javadoc

To generate the technical documentation of the Java code using Javadoc, run the command `mvn javadoc:javadoc`

The generated files will be stored in target/site/apidocs. To view the documentation, open the index.html file located in that directory in your browser.

## Unit Testing

Run `mvn test` to execute the unit tests with JUnit.

## Building the project

Run the command `mvn clean install`, the generated artifacts will be stored in the target/ folder.




