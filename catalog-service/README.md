# Catalog Service
This service is responsible for canonicalizing products and grouping equivalent products together to provide users with prices offered by different sellers for the same product. The canonicalized product title is stored and used across the catalog to maintain a consistent product name across multiple sellers.

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
- spring-boot-starter-amqp: Publishing message to RabbitMQ
- spring-boot-starter-test: Writing integration tests for APIs
- spring-boot-testcontainers: Managing test containers for integration tests.
- spring-boot-webmvc-test: Creating clients for tests
- junit-jupiter: Annotations for wiring Junit with Test containers
- mongodb: Implementation for MySQL test containers 
- lombok: Generating boiler plate code
- commons-spring: [refer]()
- mapstruct: Generating mappers from one model to another.
- jacoco-maven-plugin: Creating code coverage reports

### Product Created Flow
- Consume message from the product.actions.queue
- Route to ProductCreatedHandler
- Execute Canonicalization Pipeline steps (Normalization, Extraction, Canonicalization)
- Convert to Catalog Document
- Send message to products.ack.queue with the familyId and variantId

### Product Updated Flow
- Consume message from the product.actions.queue
- Route to ProductUpdatedHandler
- Execute Canonicalization Pipeline steps (Normalization, Extraction, Canonicalization)
- Convert to Catalog Document
- Send message to products.ack.queue with the familyId and variantId

### Handling out of order events
To prevent inconsistencies because of out of order event we will keep track of the event timestamp and product ID pair in Redis Cache and only allow events with timestamp greater than the current to execute. 

## Canonicalization Pipeline
### Normalization
- Transforming the text to lowercase
- Removing any extra whitespaces from the text
- Removing any special characters from the text
```
Example: 

Input: Apple MacBook Pro - Red, RAM 48GB, Storage 1024 GB 
Output: apple macbook pro red ram 48gb storage 1024gb
```

### Extraction and Sorting
- Excluding the brand and model which are mandatory attributes we extract the attributes from the product which match the variant attributes of category of the product.
- Sorting the data based on the increasing order of the key to get a deterministic ordering.
```
Input: {
    "brand": "apple",
    "model": "macbook pro",
    "attributes": {
        "storage": "1024gb",
        "ram": "48gb",
        "color": "red",
        "processor": "M5",
        "displaySize": "16"
    },
    category: {
        "id": "asda",
        "name": "Laptop",
        "variantAttributes": {
            "color",
            "ram",
            "storage",
            "processor"
        }
    }
}
Intermediate: [
    {"brand": "apple"}, 
    {"model": "macbook pro"}, 
    {"color": "red"}, 
    {"ram": "48gb"},
    {"processor": "m5"},
    {"storage": "1024gb"}
]
Output: 
familyAttributes: [
    {"brand": "apple"}, 
    {"model": "macbook pro"}
]

variantAttributes: [
    {"color": "red"}, 
    {"processor": "m5"},
    {"storage": "1024gb"},
    {"ram": "48gb"}
]
```

### Canonicalization
- The extracted attributes are converted into a 2 canonical representation. Initially, the canonical representation will be generated using deterministic key-value concatenation.
    - familyId: (brand, model)
    - variantId: (cateogory based attributes)
- Generate a SHA256 hash using the canonical representation.
```
familyId representation: brand=apple|model=macbook pro
variantId representation: |color=red|processor=m5|storage=1024gb|ram=48gb
Hash: SHA256(canonical representation)
```

### Store in MongoDB
- Store the space-separated sentence case canonical title, deterministic canonical representation, SHA256 hash, and extracted attributes in the MongoDB [catalog](#catalog-collection) collection
- Why? Since product attributes can be dynamic

## API Specs

### GET /catalogs/{categoryId}
```
Response Body: {
    catalogs: [
        {
            "name": "Apple MacBook Pro",
            "familyId": "canonical SHA256",
            "variantId": "canonical SHA256",
            "attributes": {
                "storage": "1024gb",
                "ram": "48gb",
                "processor": "M5",
            }
        },
        .
        .
        ., 
        {
            "name": "Apple MacBook Pro",
            "familyId": "canonical SHA256",
            "variantId": "canonical SHA256",
            "attributes": {
                "storage": "1024gb",
                "ram": "48gb",
                "processor": "M5",
            }
        }
    ],
    "lastOffset": "ObjectId",
    "totalCount": "2000"
}
```

## Schema
### catalog collection
- _id ObjectId
- name String
- categoryId String 
- familyIdRepresentation String
- variantIdRepresentation String
- familyId String
- variantId String 
- attributes JSON
- createdAt ISODateTime
- updatedAt ISODateTime

Index on (familyId + variantId, unique)

### product action message
- id String
- brand String
- model String
- attributes Json
- category Category Details
- action String
- createdAt timestamp
- version String

### product ack message
- productId String
- familyId String
- variantId String
- createdAt timestamp
- version String