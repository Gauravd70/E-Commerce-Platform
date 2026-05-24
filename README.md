# E-Commerce-Platform
[System Design] E-Commerce Platform such as Amazon, Flipkart, Myntra, etc.

# Contents
- [Tech Stack](#tech-stack)
- [Setup](#setup)
- [High Level Design (HLD)](#high-level-design-hld)
    - [Functional Requirements](#functional-requirements)
    - [Non-Functional Requirements](#non-functional-requirements)
    - [Services](#services)
- [Deployment](#deployment)

# Tech stack
- Java 17
- SpringBoot 4.0.6
- MySQL 8.0.36
- MongoDB 8.3.2
- Test Containers
- Grafana
- Prometheus
- Docker
- Kubernetes

# Setup
- Ubuntu Server with 16GB RAM and 512GB SSD
- Self hosted GitHub Runner
- JFrog Artifactory OSS for managing custom dependencies
- Docker Engine
- Minikube
- Nginx

# High Level Design (HLD)
## Functional Requirements
### Login/Signup
1. User should be able to login to the platform
2. User should be able to logout to the platform
3. User should be able to register to the platform

### Products/Cart
1. User should be able to view the products catalogue
2. User should be able search a product
3. User should be able to view the product details
4. User should be able to add the product to the cart
5. User should be able to remove a product from the cart
6. User should be able to checkout the card
7. User should be able to make the payment and confirm the order.
8. Users should get product recommendations based on their activity
9. User should be able to view the different prices offered by different sellers for the same product

### Observability
1. Platform admin should be able to see the system performance metrics
2. Platform admin should be able to see the system health
3. Each service should generate relevant logs for debugging
4. Each service should generate events for analytics

### Monitoring
1. Alerts should be raised based on relevant triggers

## Non-Functional Requirements
1. Low latency
2. Highly Available
3. Fault Tolerant
4. High consistency
6. Scalable

## Services
- [Auth Service](auth-service/README.md)
- [Product Service](product-service/README.md)
- [Search Service](#search-service)
- [Recommendation Service](#recommendation-service)
- [Catalog Service](#catalog-service)
- [Inventory Service](#inventory-service)
- [Checkout Service](#checkout-service)
- [Payment Gateway](#payment-gateway)
- [Cart Service](#cart-service)
- [Analytics Service](#analytics-service)

## Search Service
This service is responsible for searching product based on the provided input. This service uses fuzzy search to get the most probable word for the provided input and return the search result for it. It also makes use of elastic search to search for text based product search. 

### Fuzzy Search
Fuzzy search is an alogrithm where we find the [Levenshtein distance](https://medium.com/@ethannam/understanding-the-levenshtein-distance-equation-for-beginners-c4285a5604f0) between the input and a probable world. The word with the least distance is considered as the input user wanted to enter and results are returned based on the same.

### API Specs

- POST /v1/products/search
```
Query params:
searchText = url encoded string
sortBy = (price|rating)
sortOrder = (asc|desc)

Response Body: 
{
    products: [
        {
            "id": "abcd123",
            "thumbnail": "image URL",
            "name": "asdasd",
            "price": 20.0
        },
        .
        .
        .,
        {
            "id": "abcd123",
            "thumbnail": "image URL",
            "name": "asdasd",
            "price": 20.0
        }
    ],
    "count": 20,
    "totalCount": 100,
    "firstOffset": "abcd123",
    "lastOffset": "abcd123"
}
```

## Recommendation Service
This service is responsible for providing product suggestions based on the users past activities.

## Catalog Service
This service is responsible for normalization of the product and group them to provide the user with the different prices offered by different sellers for the same product

## Inventory Service
Manages the products inventory

## Checkout Service
This service is responsible for managing the checkout session. Starts a checkout session which keeps tracks of the various steps completed in the checkout session. The steps involved in a checkout are as belows:
1. Check the item availablity
2. Enter Delivery details like name, address, number, email.
3. Show final bill
4. Select payment method
5. Final check for item availability 
5. Redirect to payment gateway
6. Wait for payment confirmation
7. Show order details like order Id, expected delivery date, delivery address, payment details, etc.

### API Specs

- POST /v1/checkout/{cartId}
```
200 OK + checkout session cookie
```

## Payment Gateway
This service is responsible for handling payments using the third party payment providers.

## Cart Service
This service is responsible for keeping track of the products in a cart for logged in users.

### API Specs

- POST /v1/carts
```
Request Body:
{
    "cartName": "casdas"
}
```
- PUT /v1/carts/{cartId} (partial updates)
```
Request Body:
{
    "cartName": "casdas"
}
```
- GET /v1/carts/{cartId}
```
Request Body:
{
    "products": [
        {
            "id": "abcd123",
            "quantity": 2
        }
        .
        .
        .,
        {
            "id": "abcd123",
            "quantity": 1
        }
    ]
}
```
- DELETE /v1/carts/{cartId} 
```

```
- POST /v1/carts/{cartId}/{productId}
```
Request Body:
{
    "quantity": 2
}
```

- DELETE /v1/carts/{cartId}/{productId}
```
```

- PATCH /v1/cart/{cartId}/{productId}
```
{
    "quantity": 1
}
```

## Analytics Service
This service keeps tracks of analytics.

### API Specs

## Review Service
This service manages all the reviews for the products.

### API Specs
- POST /v1/reviews/{productId}
```
{
    "rating": 4,
    "comment": "",
    images: [url1,....url2]
}
```
- GET /v1/reviews/{productId}
```
{
    reviews: [
        {
            "rating": 4,
            "title": "",
            "comment": "",
            "images": [url1,...url2] 
        }
        .
        .
        .
        {
            "rating": 4,
            "title": "",
            "comment": "",
            "images": [url1,...url2] 
        }
    ]
    "count": 10,
    "totalReviews": 1000,
    "overallRating": 4.5,
    "firstOffset": "reviewId",
    "lastOffset": "reviewId"
}
```
- PUT /v1/reviews/{productId}/{reviewId} (replace)
```
{
    "rating": 4,
    "title": "",
    "comment": "",
    "images": [url1,...url2] 
}
```
- DELETE /v1/reviews/{productId}/{reviewId}
```
```

## Checkout Cookie Model
```
{
    "userId": "abcd123",
    "cartId": "asdasd1123",
    "startedAt": "timestamp",
    "expiresAt": "timestamp"
}
```
  
# Deployment
- Components of each service
    - Dockerfile: For building image
    - k8s manifest: For deploying on minikube
    - GitHub workflows: For CI and CD

In this project we are following a push based CI/CD approach where a github workflow gets triggered when certain files being watched by the workflow changes. On detecting a change the CI pipeline is triggered which builds the service, creates images tagged using the commit sha and latest, push the image to DockerHub. Once the CI pipeline completes successfully it triggers the CD pipeline which deploys the service. 
