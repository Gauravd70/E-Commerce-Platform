# Product Service
This service is responsible for managing the products. 

## Content
- [Low Level Design](#low-level-design-lld)
- [API specs](#api-specs)
- [Schema](#schema)

## Low Level Design (LLD)
### Dependencies
- spring-boot-starter-web: Implementing MVC based REST APIs
- spring-boot-starter-validation: Validating API request body
- spring-boot-starter-data-jpa: Implementing data access layer 
- mysql-connector-j: MySQL Connector implementation required by Data JPA
- spring-boot-starter-actuator: Implementing service health APIs
- spring-boot-starter-security: Implementing authentication and authorization 
- spring-boot-starter-test: Writing integration tests for APIs
- spring-boot-testcontainers: Managing test containers for integration tests.
- spring-boot-webmvc-test: Creating clients for tests
- junit-jupiter: Annotations for wiring Junit with Test containers
- mysql: Implementation for MySQL test containers 
- lombok: Generating boiler plate code
- commons-spring: [refer]()
- mapstruct: Generating mappers from one model to another.
- jacoco-maven-plugin: Creating code coverage reports
- flyway-core
- flyway-mysql
- spring-boot-starter-flyway
- jjwt-api
- jjwt-impl

## API Specs

### POST /products/v1
```
Request Body:
{
    "name": "asdasd",
    "price": 20.0,
    "quantity": 5,
    "description": "adsanda"
}

Response Body:
201 CREATED {
    "id": "asda",
    "message": "Product added successfully"
}
```

### GET /products/v1/{productId}
```
Response Body:
200 OK {
    "id": "abcd123",
    "name": "asdasd",
    "price": 20.0,
    "quantity": 5,
    "description": "adsanda",
    "images": [
        {
            "id": 1234,
            "url": "https://www.image1.com",
            "type": "Thumbnail"
            "displayOrder": 1
        },
        .
        .
        .,
        {
            "id": 43333,
            "url": "https://www.imagen.com",
            "type": "Gallery"
            "displayOrder": n
        }
    ],
    "categories": [
        cat1,
        .
        .,
        catn
    ]
}
```

### PUT /products/v1/{productId}
```
Request Body:
{
    "name": "asdasd",
    "price": 20.0,
    "quantity": 5,
    "description": "adsanda"
}

Response Body:
200 OK {
    "message": "Product updated successfully"
}
```

### DELETE /products/v1/{productId}
```
Response Body:
200 OK {
    "message": "Product deleted successfully"
}
```

### PATCH /products/v1/{productId}/status
```
Request Body: 
{
    "active": true/false
}

Response Body:
200 OK
```

### GET /products/v1/{productId}/status
```
Response Body:
200 OK {
    "active": true
}
```

### POST /products/v1/{productId}/images
```
Request Body:
[
    {
        "url": "url",
        "type": "thumbnail|gallery",
        "displayOrder": 1
    },
    .
    .
    .,
    {
        "url": "url",
        "type": "gallery",
        "displayOrder": n
    }
]

Response Body:
200 OK
```

### DELETE /products/v1/{productId}/images/{imageId}
```
Response Body:
200 OK
```

### POST /categories/v1
```
Request Body:
{
    "name": "cat1"
}

Response Body:
201 CREATED {
    "id": "21312",
    "message": "Category created successfully"
}
```

### GET /categories
```
Response Body:
200 OK {
    categories: [
        {
            "id": "dada",
            "name": "cat1"
        },
        .
        .
        {
            "id": "afa",
            "name": "catn"
        }
    ]
}
```

### GET /categories/{categoryId}
```
Response Body:
200 OK {
    "id": "dada",
    "name": "cat1"
}
```

### PUT /categories/{categoryId}
```
Request Body:
{
    "name": "cat1"
}

Response Body:
200 OK {
    "message": "Category updated successfully"
}
```

### DELETE /categories/{categoryId}
```
Response Body:
200 OK 
```

### POST /products/v1/{productId}/categories
```
Request Body:
[catId1,...., catIdn]

Response Body:
200 OK
```

### DELETE /products/v1/{productId}/categories/{categoryId}
```
Response Body:
200 OK
```

## Schema

### product table
- id long (Primary Key Auto Increment)
- name varchar
- price decimal(10, 2)
- description text
- seller_id long
- group_id long
- active boolean
- created_at timestamp
- updated_at timestamp

### category table
- id long (Primary Key Auto Increment)
- name varchar (Unique)

### product_category_mapping table
- product_id long (Foreign Key)
- category_id long (Foreign Key)

PRIMARY KEY (productId + categoryId)

### product_image_mapping table
- id long (Primary Key Auto Increment)
- product_id long (Foreign Key)
- url varchar
- type varchar
- display_order int