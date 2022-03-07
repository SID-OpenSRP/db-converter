
# db-converter

**Repo Owner:** Levi [@muhammad-levi](https://github.com/muhammad-levi)

Spring Boot App to convert from Data Collecting DB to more readable and operable DB

## Overview

`db-converter` app is a middleware data transformation component, written in Java as a background application with scheduled tasks, using [Spring Boot](https://spring.io/projects/spring-boot) framework. It accepts and converts data from a DB objects into another DB objects identified by custom query that can be written in the [source-database.yml](src/main/resources/config/source-database.yml).

## Points of Interest
Have a look at:
1. [source-database.yml](src/main/resources/config/source-database.yml) file.
2. [destination-database.yml](src/main/resources/config/destination-database.yml) file.

## Use

### Development

Gradle is used as the software project management tool.

### Building

Use `./gradlew bootJar` to build the executable jar file. Gradle will build executable jar with dependencies and place them in the `build/libs/` directory.

### Running

Current version of the `db-converter` is a standalone background application that expects configuration at the runtime - DB user credentials for both the source (read-only access is preferred) and the destination (must have write access). By default, application will run on port 8080. Alternative port can be specified using `server.port` option.

`java -jar db-converter-${DB-CONVERTER_VERSION}.jar --server.port=9000
