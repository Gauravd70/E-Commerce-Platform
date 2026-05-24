# Auth Service
This service is responsible for authenticating and authorizing the users. 

## Content
- [Low Level Design](#low-level-design-lld)
- [API specs](#api-specs)
- [Schema](#schema)
- [Cookies](#cookies)

## Low Level Design (LLD)
### Dependencies
- spring-boot-starter-web: Implementing MVC based REST APIs
- spring-boot-starter-validation: Validating API request body 
- spring-boot-starter-data-jpa: Implementing data access layer 
- mysql-connector-j: MySQL Connector implementation required by Data JPA
- spring-boot-starter-actuator: Implementing service health APIs
- spring-boot-starter-security: Implementing authentication and authorization 
- spring-boot-starter-test: Writing integration tests for APIs
- spring-boot-starter-webmvc-test: Create MockMvc client
- spring-boot-testcontainers: Managing test containers for integration tests.
- junit-jupiter: Annotations for wiring Junit with Test containers
- mysql: Implementation for MySQL test containers 
- lombok: Generating boiler plate code
- commons-spring: [refer]()
- mapstruct: Generating mappers from one model to another.
- jacoco-maven-plugin: Creating code coverage reports
- jjwt-api
- jjwt-impl
- jjwt-jackson

### Login Flow
Login API skips the JWT validation filter since it does not have any access token and is configured to be permitted at all times in the SpringBoot security filter chain.

1. Call POST [/v1/login](#post-v1signup)
2. Validate request body
    1. If invalid return 400 BAD REQUEST
    2. If valid, go to 3
3. Check if user exists and password matches the stored password
    1. If false, return 401 UNAUTHORIZED
    2. If true, go to 4
4. Create the access and refresh [JWT token](#jwt-token)
5. Create cookies for both the tokens and add to response.
6. Return 200 OK

### Signup Flow
Sign up API skips the JWT validation filter since it does not have any access token and is configured to be permitted at all times in the SpringBoot security filter chain.

1. Call POST [/v1/signup](#post-v1login)
2. Validate request body
    1. If invalid return 400 BAD REQUEST
    2. If valid, go to 3
3. Check if the password matches
    1. If does not match, return 400 BAD REQUEST
    2. If matches, go to point 4
4. Create User entity from the request body and set the encoded password and roles.
5. Persist the entity to the users table
    1. If email already exists, return 400 BAD REQUEST
    2. If email does not exist, go to 6
6. Return 200 OK 

### Logout Flow [spec](#post-v1logout)
Logout API requires an access token and will be filtered by the SpringBoot security filter chain if a token is not found.

1. Call [/v1/logout](#post-v1logout) with access token cookie
2. Check if the token is valid
    1. If invalid, return 401 UNAUTHORIZED
    2. If valid, go to 3
3. Check if the user exists
    1. If does not exist, return 401 UNAUTHORIZED
    2. If exists, go to 4
5. Create a new access and refresh token with the max age as 0 and add to response
5. Return 200 OK

## API Specs
### POST /v1/login 
```
Request Body: 
{
    "username": "asda@gmail.com",
    "password": "sadasd"
}

Response Body:

200 OK + ACCESS_TOKEN (cookie) 

401 UNAUTHORIZED {
    "message": "Incorrect username or passoword."
}
```
### POST /v1/logout
```
200 OK

401 UNAUTHORIZED
```
### POST /v1/signup/user or /v1/signup/user
```
Request Body: 
{
    "firstName": "asdsad",
    "lastName": "asdasd",
    "username": "asda@gmail.com",
    "password": "sadasda",
    "confirmPassword": "asdasda"
}

Response Body:

200 OK {
    "message": "User created successfully"
}

400 BAD REQUEST {
    "message": "User already exists"
}

400 BAD REQUEST {
    "message": "Passwords do not match"
}
```

## Schema

### users table
- id INT AUTO_INCREMENT PRIMARY KEY
- firstname VARCHAR(20) NOT NULL
- lastname VARCHAR(20)
- username VARCHAR(100) NOT NULL UNIQUE
- password VARCHAR(256) NOT NULL
- created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
- updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

### roles table
- id INT AUTO_INCREMENT PRIMARY KEY
- name VARCHAR(20) NOT NULL UNIQUE

### user_role_mappings table
- user_id INT NOT NULL FOREIGN KEY
- role_id INT NOT NULL FOREIGN KEY

### JWT token
```
{
    "sub": "abcd123",
    "roles": [role1,..., role2],
    "iat": timestamp
    "exp": timestamp
}
```

## Cookies
- ACCESS_TOKEN = JWT
- REFRESH_TOKEN = JWT
