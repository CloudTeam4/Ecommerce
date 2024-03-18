CREATE TABLE IF NOT EXISTS member
(
    id
               BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    email
               VARCHAR(255) NOT NULL,
    password   VARCHAR(255) NOT NULL,
    phone      VARCHAR(255) NOT NULL,
    address    VARCHAR(255) NOT NULL,
    nickname   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    cart_id    BIGINT,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS product
(
    item_id
                BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    category
                VARCHAR(255) NOT NULL,
    name        VARCHAR(255),
    explanation TEXT,
    price       INT          NOT NULL,
    image       VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS cart
(
    id
        BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    total_quantity
        INT
        NOT
            NULL
);

CREATE TABLE IF NOT EXISTS coupon
(
    coupon_id
                   BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    member_id
                   BIGINT
                             NOT
                                 NULL,
    name
                   VARCHAR(255),
    explanation    TEXT,
    deducted_price INT       NOT NULL,
    status         VARCHAR(255),
    type           BOOLEAN,
    applicable     VARCHAR(255),
    quantity       INT       NOT NULL,
    created_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY
        (
         member_id
            ) REFERENCES member
        (
         id
            )
);

CREATE TABLE IF NOT EXISTS orders
(
    id
                 BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    payment_method
                 VARCHAR(255) NOT NULL,
    total_price  INT,
    status       VARCHAR(255) NOT NULL,
    order_date   TIMESTAMP    NOT NULL,
    arrival_date TIMESTAMP,
    member_id    BIGINT       NOT NULL,
    FOREIGN KEY
        (
         member_id
            ) REFERENCES member
        (
         id
            )
);

CREATE TABLE IF NOT EXISTS coupon_box
(
    coupon_box_id
        BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    member_id
        BIGINT
        NOT
            NULL,
    coupon_id
        BIGINT
        NOT
            NULL,
    FOREIGN
        KEY
        (
         member_id
            ) REFERENCES member
        (
         id
            ),
    FOREIGN KEY
        (
         coupon_id
            ) REFERENCES coupon
        (
         coupon_id
            )
);

CREATE TABLE IF NOT EXISTS cart_item
(
    id
        BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    cart_id
        BIGINT,
    product_id
        BIGINT,
    quantity
        INT
        NOT
            NULL,
    FOREIGN
        KEY
        (
         cart_id
            ) REFERENCES cart
        (
         id
            ),
    FOREIGN KEY
        (
         product_id
            ) REFERENCES product
        (
         item_id
            )
);

CREATE TABLE IF NOT EXISTS order_detail
(
    id
        BIGINT
        AUTO_INCREMENT
        PRIMARY
            KEY,
    quantity
        INT
        NOT
            NULL,
    orders_id
        BIGINT,
    item_id
        BIGINT,
    FOREIGN
        KEY
        (orders_id
            ) REFERENCES orders
        (
         id
            ),
    FOREIGN KEY
        (
         item_id
            ) REFERENCES product
        (
         item_id
            )
);

