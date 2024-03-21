-- Member Table
CREATE TABLE member
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone    VARCHAR(255) NOT NULL,
    address  VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    role     VARCHAR(255) NOT NULL,
    cart_id  BIGINT UNIQUE
);

-- Cart Table
CREATE TABLE cart
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    total_quantity INT DEFAULT 0
);

-- CartItem Table
CREATE TABLE cart_item
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id    BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity   INT    NOT NULL,
    FOREIGN KEY (cart_id) REFERENCES cart (id),
    FOREIGN KEY (product_id) REFERENCES product (item_id)
);

-- Product Table
CREATE TABLE product
(
    item_id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    category    VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    explanation TEXT,
    price       INT          NOT NULL,
    image       VARCHAR(255)
);

-- Coupon Table
CREATE TABLE coupon
(
    coupon_id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id   BIGINT NOT NULL,
    name        VARCHAR(255),
    explanation TEXT,
    price       INT,
    status      VARCHAR(255),
    type        BOOLEAN,
    statufields VARCHAR(255),
    quantity    INT,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- CouponBox Table
CREATE TABLE coupon_box
(
    coupon_box_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_id     BIGINT NOT NULL,
    coupon_id     BIGINT NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id),
    FOREIGN KEY (coupon_id) REFERENCES coupon (coupon_id)
);

-- Order Table
CREATE TABLE orders
(
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_method VARCHAR(255) NOT NULL,
    total_price    INT,
    status         VARCHAR(255) NOT NULL,
    order_date     TIMESTAMP    NOT NULL,
    arrival_date   TIMESTAMP,
    member_id      BIGINT       NOT NULL,
    FOREIGN KEY (member_id) REFERENCES member (id)
);

-- OrderDetail Table
CREATE TABLE order_detail
(
    id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    quantity INT    NOT NULL,
    order_id BIGINT NOT NULL,
    item_id  BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (item_id) REFERENCES product (item_id)
);

-- Alter Table to add foreign key for Cart in Member
ALTER TABLE member
    ADD CONSTRAINT fk_cart
        FOREIGN KEY (cart_id)
            REFERENCES cart (id);
