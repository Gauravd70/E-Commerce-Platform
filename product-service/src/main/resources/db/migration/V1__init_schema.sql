CREATE TABLE IF NOT EXISTS products (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT,
    seller_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    active BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product_image_mappings (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    product_id BIGINT NOT NULL,
    url TEXT,
    type varchar(20),
    display_order INT

    CONSTRAINT fk_pim_products
        FOREIGN KEY(product_id)
        REFERENCES products(id)
        ON DELETE CASCADE;
);

CREATE TABLE IF NOT EXISTS categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS product_category_mappings (
    product_id INT NOT NULL,
    category_id INT NOT NULL,

    PRIMARY KEY(product_id, category_id),

    CONSTRAINT fk_pcm_products
        FOREIGN KEY(product_id)
        REFERENCES products(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_pcm_categories
        FOREIGN KEY(category_id)
        REFERENCES categories(id)
        on DELETE CASCADE
);