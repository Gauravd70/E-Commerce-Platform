# Auth Service
This service is responsible for authenticating and authorizing the users. Since users data won't be changing very frequently we will be using MySQL to store the users data.

Schema: [Refer](#users-table-schema)

## API Specs
- POST /v1/login 
```
Request Body: 
{
    "username": "asda@gmail.com",
    "password": "sadasd"
}

Response Body:

200 OK + session cookie 

401 Unauthorized
```
- POST /v1/logout
```
200 OK
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

400 Bad Request {
    "message": "User already exists"
}
```

## Users table schema
- id (Auto increment)
- firstName varchar
- lastName varchar
- username varchar (Primary Key)
- password varchar (stored as SHA256 hash)

## Session Cookie model
```
{
    "userId": "abcd123"
}
```