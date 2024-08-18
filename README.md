# RestAPIStarWars
A RESTFul Java API based on Spring Boot that provides information about characters from the Star Wars saga.

## Prerequisites

- **Java 17**: Ensure you have Java 17 installed on your machine.
- **Maven**: Required to build the JAR file.
- **Docker**: Required to build and run the Docker image.

## Resources

In the `src/main/resources` directory, you will find:

1. **Postman Collection**: A Postman collection is included with all the tests for the API endpoints. This collection can be imported into Postman to easily test the API.

    - **File**: `RestAPIStarWars.postman_collection.json`
    - **Usage**: Import the file into Postman by selecting `Import` in Postman and choosing the `RestAPIStarWars.postman_collection.json` file from the `resources` directory.

2. **Swagger Documentation**: The API is fully documented using Swagger. The Swagger YAML file provides a detailed description of each endpoint, including request parameters, response formats, and error codes.

    - **File**: `RestAPIStarWars.yaml`
    - **Usage**: The Swagger documentation can be viewed by importing the `RestAPIStarWars.yaml` file into a Swagger UI tool or by using online Swagger editors such as [Swagger Editor](https://editor.swagger.io/).


## Project Setup

### Spring Boot Configuration

Make sure your Spring Boot application is configured to run on port 8080 (or your preferred port). You can set this in the `application.properties` or `application.yml` file:

**`application.properties`**:
```properties
server.port=8080
```

### Building the JAR FILE
Build the **JAR** file of your application using Maven:
```bash
maven clean package
```
This command will generate a JAR file in the **target** directory.

### Build the Docker Image
Build the Docker image with the following command:
```bash
docker build -t diverger-star-wars-api .
```
Here, *diverger-star-wars-api* is the name of the Docker image you are creating.

### Run the Docker Image
To run your application in a Docker container, use the following command:
```bash
docker run -p 8081:8080 diverger-star-wars-api
```