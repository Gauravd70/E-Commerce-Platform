# Canonicalization Service
This service is responsible for canonicalizing products and grouping equivalent products together to provide users with prices offered by different sellers for the same product. The canonicalized product title is stored and used across the catalog to maintain a consistent product name across multiple sellers.

## Content
- [Low Level Design](#low-level-design-lld)
- [API specs](#api-specs)
- [Schema](#schema)

## Low Level Design (LLD)
### Dependencies

## Canonicalization Pipeline

### Normalization
- Transforming the text to lowercase
- Removing any extra whitespaces from the text
- Removing any special characters from the text
```
Example: 

Input: Apple MacBook Pro - Red, RAM 48GB 
Output: apple macbook pro red ram 48gb
```

### Extraction
- Extracting attributes such as brand, model, color  
- Sorting the data based on the increasing order of the key to get a deterministic ordering.
```
Input: apple macbook pro red ram 48gb
Intermediate: [
    {"brand": "apple"}, 
    {"model": "macbook pro"}, 
    {"color": "red"}, 
    {"ram": "48gb"}
]
Output: [
    {"brand": "apple"}, 
    {"color": "red"}, 
    {"model": "macbook pro"}, 
    {"ram": "48gb"}
]
```

### Canonicalization
- The extracted attributes are converted into a canonical representation. Initially, the canonical representation will be generated using deterministic key-value concatenation.
- Generate a SHA256 hash using the canonical representation.
```
Canonical representation: brand=apple|color=red|model=macbook pro|ram=48gb
Hash: SHA256(canonical representation)
```

### Store in MongoDB
- Store the space-separated canonical title, deterministic canonical representation, SHA256 hash, and extracted attributes in the MongoDB [canonical_products](#canonical_products-collection) collection
- Why? Since product attributes can be dynamic

## API Specs

## Schema
### canonical_products collection
_id ObjectId
name String
canonicalName String
groupId String
attributes Nested JSON (dynamic)
