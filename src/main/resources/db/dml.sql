INSERT INTO member (email, password, phone, address, nickname, role)
VALUES ('admin@example.com', 'securepassword', '1234567890', '123 Admin Street', 'AdminUser', 'ADMIN');

-- INSERT INTO member (email, password, phone, address, nickname, role)
-- SELECT
--     CONCAT('user', number, '@example.com') AS email,
--     'password' AS password,
--     CONCAT('123456', LPAD(number, 2, '0')) AS phone,
--     CONCAT('Address ', LPAD(number, 2, '0')) AS address,
--     CONCAT('User', number) AS nickname,
--     'CUSTOMER' AS role
-- FROM
--     (SELECT n FROM (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) AS numbers) AS a
--         CROSS JOIN (SELECT n FROM (SELECT 1 AS n UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION SELECT 5) AS numbers) AS b
--     LIMIT 100;
--
--
-- INSERT INTO product (category, name, explanation, price, image)
-- VALUES
--     ('Electronics', 'Smartphone', 'A powerful smartphone with high-resolution display', 500, 'smartphone.jpg');
--
-- INSERT INTO orders (payment_method, total_price, status, order_date, arrival_date, member_id)
-- VALUES
--     ('Credit Card', 400, 'PROCESSING', CURRENT_TIMESTAMP, NULL, 1);
--
-- -- 주문 상세 데이터 삽입
-- INSERT INTO order_detail (quantity, orders_id, item_id)
-- VALUES
--     (2, 1, 1);