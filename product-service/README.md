# Product Service
This service is responsible for managing the products and category definitions.

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
1. Call POST [/products/v1](#post-productsv1)
2. Validate request body
    1. If invalid, return 400 BAD REQUEST
    2. If valid, go to step 3
3. TODO: Send an event to Catalog Service to generate the familyId and the variantId for the product
4. Create product document from the request
5. Persist the document to products collection
6. TODO: Send an event to inventory service
7. Return 200 OK 

### Update Product Flow
1. Call POST [/products/v1/{productId}](#patch-productsv1productid)
2. Validate productId
    1. If invalid, return 400 BAD REQUEST
    2. If valid, go to step 3
3. TODO: Send an event to Catalog Service to generate the familyId and the variantId for the product
4. Create product document from the request
5. Persist the document to products collection
6. TODO: Send an event to inventory service
7. Return 200 OK 

### Delete Product Flow
1. Call POST [/products/v1/{productId}](#delete-productsv1productid)
2. Validate productId
    1. If invalid, return 400 BAD REQUEST
    2. If valid, go to step 3
3. TODO: Send an event to Catalog Service to remove the familyId and the variantId for the product
4. Delete the product from products collection
5. TODO: Send an event to inventory service
6. Return 200 OK 

## API Specs

### POST /products/v1
```
Request Body:
{
    "brand": "asdasd",
    "model": "asdad",
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
    "categoryId": "cat1",
    "attributes": {
        "storage": "1024gb",
        "ram": "48gb",
        "color": "red",
        "processor": "M5",
        "displaySize": "16"
    }
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
    "brand": "asdasd",
    "model": "asdad",
    "price": 20.0,
    "description": "adsanda",
    "sellerId": "asda",
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
    "categoryId": "cat1",
    "attributes": {
        "storage": "1024gb",
        "ram": "48gb",
        "color": "red",
        "processor": "M5",
        "displaySize": "16"
    },
    "createdAt": Date&Time,
    "updatedAt": Date&Time
}
```

### PATCH /products/v1/{productId}
```
Request Body:
{
    "brand": "asdasd",
    "model": "asdad",
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
    "categoryId": "cat1",
    "attributes": {
        "storage": "1024gb",
        "ram": "48gb",
        "color": "red",
        "processor": "M5",
        "displaySize": "16"
    }
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
    "name": "cat1",
    "variantAttributes": {
        "storage",
        "memory",
        "color",
        "processor"
    }
}

Response Body:
201 CREATED {
    "id": "21312",
    "message": "Category created successfully"
}
```

### GET /products/categories/v1
```
Response Body:
200 OK {
    categories: [
        {
            "id": "dada",
            "name": "cat1",
            "variantAttributes": {
                "storage",
                "memory",
                "color",
                "processor"
            }
        },
        .
        .
        {
            "id": "afa",
            "name": "catn",
            "variantAttributes": {
                "storage",
                "memory",
                "color",
            }
        }
    ]
}
```

### GET /products/categories/v1/{categoryId}
```
Response Body:
200 OK {
    "id": "dada",
    "name": "cat1",
    "variantAttributes": {
        "storage",
        "memory",
        "color",
        "processor"
    }
}
```

### PATCH /products/categories/v1/{categoryId}
```
Request Body:
{
    "name": "cat1",
    "active": true/false,
    "variantAttributes": {
        "storage",
        "memory",
        "color",
        "processor"
    }
}

Response Body:
200 OK {
    "message": "Category updated successfully"
}
```

### DELETE /products/categories/v1/{categoryId}
```
Response Body:
200 OK 
```

## Schema

### products collection document
- id ObjectId
- brand String
- model String
- price double
- description String
- sellerId long
- active boolean
- createdAt ISODate
- updatedAt ISODate
- categoryId String 
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
- variantAttributes List of String
- createdAt Date&Time
- updatedAt Date&Time

### canonical product message
brand String
model String
attributes Json
category Category Details
