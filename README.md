# RestAPIStarWars
A RESTFul Java API based on Spring Boot that provides information about characters from the Star Wars saga.

## Prerequisites

- **Java 17**: Ensure you have Java 17 installed on your machine.
- **Maven**: Required to build the JAR file.
- **Docker**: Required to build and run the Docker image.

## Architecture Overview

This project uses Hexagonal Architecture (Ports and Adapters) to organize the code.

### Key Points:
- **Domain Layer**: Contains core business logic and domain models (e.g., `CharacterDomain`).
- **Application Layer**: Manages use cases and interacts with domain models through defined interfaces (ports).
- **Infrastructure Layer**: Handles communication with external systems like APIs and databases through adapters.

### Benefits:
- **Separation of Concerns**: Keeps business logic separate from external systems.
- **Flexibility**: Allows easy changes to external systems without affecting core logic.
- **Testability**: Enables testing of core logic independently of external systems.

This architecture helps keep the application modular, maintainable, and adaptable to changes.

## Resources

In the `src/main/resources` directory, you will find:

1. **Postman Collection**: A Postman collection is included with all the tests for the API endpoints. This collection can be imported into Postman to easily test the API.

    - **File**: `RestAPIStarWars.postman_collection.json`
    - **Usage**: Import the file into Postman by selecting `Import` in Postman and choosing the `RestAPIStarWars.postman_collection.json` file from the `resources` directory.

2. **Swagger Documentation**: The API is fully documented using Swagger. The Swagger YAML file provides a detailed description of each endpoint, including request parameters, response formats, and error codes.

    - **File**: `RestAPIStarWars.yaml`
    - **Usage**: The Swagger documentation can be viewed by importing the `RestAPIStarWars.yaml` file into a Swagger UI tool or by using online Swagger editors such as [Swagger Editor](https://editor.swagger.io/).

## Usage

To use the API and retrieve information about a character, you should make an HTTP GET request to the following URL:
```
http://{host}:{port}/swapi-proxy/person-info?name={characterName}
```
### Example Request

If you want to get information about "Luke Skywalker," the request URL would be:
```
http://{host}:{port}/swapi-proxy/person-info?name=Luke%20Skywalker
```

### Parameters

- **`host`**: The domain name or IP address where the service is running.
- **`port`**: The port on which the service is listening.
- **`name`**: The name of the character you want to retrieve information about. This can be any string, not necessarily the full name of the character. For example, you can use just `Lu` to search for "Luke Skywalker" and other characters as "Luminara Unduli" . This name should be URL-encoded (e.g., spaces replaced with `%20`).

### Example Response

The response will be a JSON array of objects containing the character's information, including details such as name, birth year, gender, home planet, fastest vehicle driven, and the films they appeared in.

```json
[
   {
      "name": "Luke Skywalker",
      "birth_year": "19BBY",
      "gender": "male",
      "planet_name": "Tatooine",
      "fastest_vehicle_driven": "X-wing",
      "films": [
         {
            "name": "A New Hope",
            "release_date": "1977-05-25"
         },
         {
            "name": "The Empire Strikes Back",
            "release_date": "1980-05-21"
         }
      ]
   }
]
```

### Other Example Request

If you want to get information about "Lu," the request URL would be:
```
http://{host}:{port}/swapi-proxy/person-info?name=Lu
```
### Example Response

The response will be a JSON array of objects containing the character's information, including details such as name, birth year, gender, home planet, fastest vehicle driven, and the films they appeared in.

```json
[
   {
      "name": "Luminara Unduli",
      "gender": "female",
      "films": [
         {
            "name": "Revenge of the Sith",
            "release_date": "2005-05-19"
         },
         {
            "name": "Attack of the Clones",
            "release_date": "2002-05-16"
         }
      ],
      "birth_year": "58BBY",
      "planet_name": "Mirial",
      "fastest_vehicle_driven": "n/a"
   },
   {
      "name": "Luke Skywalker",
      "gender": "male",
      "films": [
         {
            "name": "The Empire Strikes Back",
            "release_date": "1980-05-17"
         },
         {
            "name": "A New Hope",
            "release_date": "1977-05-25"
         },
         {
            "name": "Return of the Jedi",
            "release_date": "1983-05-25"
         },
         {
            "name": "Revenge of the Sith",
            "release_date": "2005-05-19"
         }
      ],
      "birth_year": "19BBY",
      "planet_name": "Tatooine",
      "fastest_vehicle_driven": "X-wing"
   }
]
```


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