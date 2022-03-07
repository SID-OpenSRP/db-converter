# db-converter

Spring Boot App to convert from Data Collecting DB to more readable and operable DB

## Overview

`db-converter` app is a middleware data transformation component, written in Java as a background application with scheduled tasks, using [Spring Boot](https://spring.io/projects/spring-boot) framework. It accepts and converts data from a DB objects into another DB objects identified by custom query that can be written in the [source-database.yml](src/main/resources/config/source-database.yml).

## Points of Interest
Have a look at:
1. [source-database.yml](src/main/resources/config/source-database.yml) file.
2. [destination-database.yml](src/main/resources/config/destination-database.yml) file.
