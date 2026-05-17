# Auth Service
This service is responsible for authenticating and authorizing the users. Since users data won't be changing very frequently we will be using MySQL to store the users data. On login the service returns a cookie containing the JWT access token and JWT refresh token. The access token will be used to perform actions on the platform and will have a short time to live (TTL) of 60 minutes whereas the refresh token will be used to generate a new access token in case the existing access token expires.

## Content
- [API specs](#api-specs)
- [Schema](#schema)
- [Cookies](#cookies)

## API Specs
- POST /v1/login 
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
- POST /v1/logout
```
200 OK

401 UNAUTHORIZED
```
- POST /v1/signup
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

### Users Table
- id (Auto increment)
- firstName varchar
- lastName varchar
- username varchar (Primary Key)
- password varchar (stored as SHA256 hash)

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
