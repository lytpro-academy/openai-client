# Openai-Client

## Overview
The OpenAI Client is a Spring Boot application with the purpose of conducting operations related to OpenAI API. Its version is 0.0.1-SNAPSHOT.

## Java Version
The project uses Java SDK version 17.

## Spring Boot Version
This project uses Spring Boot version 3.2.3.

## Dependencies

The project uses the following dependencies:

1. spring-boot-starter-web
2. spring-ai-bom
3. spring-boot-starter-actuator
4. spring-ai-openai-spring-boot-starter
5. spring-boot-starter-test
6. springdoc-openapi-starter-webmvc-ui
7. lombok

## Building the Project

This project makes use of the Spring Boot Maven Plugin. Lombok is excluded in the plugin configuration to avoid issues during the build.

## Running and Testing the Service

Follow these steps to run the User Service:

1. Build the project with Maven: mvn clean install
2. Run Application class to run the application
3. You can test the API at: http://localhost:8081/swagger-ui/index.html#/