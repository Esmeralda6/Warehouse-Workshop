CREATE TABLE products (
    id UUID NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE warehouses (
    id UUID NOT NULL,
    name VARCHAR(255),
    warehouse_type VARCHAR(50),
    location_x INT,
    location_y INT,
    factory_id UUID,
    CONSTRAINT pk_warehouses PRIMARY KEY (id)
);

CREATE TABLE stock_items (
    id UUID NOT NULL,
    product_id UUID,
    quantity INT NOT NULL,
    warehouse_id UUID,
    minimum_quantity INT NOT NULL,

    CONSTRAINT pk_stock_items PRIMARY KEY (id),
    CONSTRAINT uq_product_warehouse UNIQUE (product_id, warehouse_id),

    CONSTRAINT fk_stock_items_product FOREIGN KEY (product_id)
        REFERENCES products (id) ON DELETE CASCADE,

    CONSTRAINT fk_stock_items_warehouse FOREIGN KEY (warehouse_id)
        REFERENCES warehouses (id) ON DELETE CASCADE
);