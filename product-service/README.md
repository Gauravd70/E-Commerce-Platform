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
- spring-boot-starter-data-mongodb: Implementing data access layer 
- spring-boot-starter-actuator: Implementing service health APIs
- spring-boot-starter-security: Implementing authentication and authorization 
- spring-boot-starter-test: Writing integration tests for APIs
- spring-boot-testcontainers: Managing test containers for integration tests.
- spring-boot-webmvc-test: Creating clients for tests
- junit-jupiter: Annotations for wiring Junit with Test containers
- mongodb: Implementation for MySQL test containers 
- lombok: Generating boiler plate code
- commons-spring: [refer]()
- mapstruct: Generating mappers from one model to another.
- jacoco-maven-plugin: Creating code coverage reports

### Create Product Flow
1. Call POST [/v1](#post-productsv1)
2. Validate request body
    1. If invalid, return 400 BAD REQUEST
    2. If valid, fo to step 3
3. TODO: Call Catalog Service to get the canonical group for the product
4. Create product document from the request
5. Persist the document to products collection
6. TODO: Send an event to inventory service
7. Return 200 OK 

### Update Product Flow

### Delete Product Flow

## API Specs

### POST /products/v1
```
Request Body:
{
    "name": "asdasd",
    "price": 20.0,
    "quantity": 5,
    "description": "adsanda",
    "images": [
        {
            "id": "ObjectId",
            "url": "https://image.com",
            "type": "thumbnail",
            "displayOrder": 1
        },
        .
        .
        .
        {
            "id": "ObjectId",
            "url": "https://image_n.com",
            "type": "thumbnail",
            "displayOrder": n
        }
    ],
    "categories": [ObjectId1,.....,ObjectIdn]
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
        cat1Id,
        .
        .,
        catnId
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
    "description": "adsanda",
    "images": [
        {
            "id": "ObjectId",
            "url": "https://image.com",
            "type": "thumbnail",
            "displayOrder": 1
        },
        .
        .
        .
        {
            "id": "ObjectId",
            "url": "https://image_n.com",
            "type": "thumbnail",
            "displayOrder": n
        }
    ],
    "categories": [ObjectId1,.....,ObjectIdn]
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

### POST /products/categories/v1
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

### GET /products/categories
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

### GET /products/categories/{categoryId}
```
Response Body:
200 OK {
    "id": "dada",
    "name": "cat1"
}
```

### PUT /products/categories/{categoryId}
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

### DELETE /products/categories/{categoryId}
```
Response Body:
200 OK 
```

## Schema

### products collection document
- id ObjectId
- name String
- price double
- description String
- sellerId long
- group_id ObjectId
- active boolean
- createdAt ISODate
- updatedAt ISODate
- categories List\<ObjectId> 
- images List<[ImageInfo](#imageinfo-document)>
- attributes JsonObject

### ImageInfo Document
- id ObjectId
- url String
- type String (thumbnail|gallery)
- displayOrder int

### categories collection document
- id ObjectId
- name String unique
- active boolean