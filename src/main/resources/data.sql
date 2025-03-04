-- Insert Roles
INSERT INTO roles (id, name) VALUES (1, 'ROLE_ADMIN'); -- Admin role
INSERT INTO roles (id, name) VALUES (2, 'ROLE_USER'); -- User role

-- Insert Users with Email
INSERT INTO users (username, email, password) VALUES
                                                  ('admin', 'admin@example.com', '$2a$10$KM2kMyJPMmGjF1UjmLzb6eX4KIdLGDkGPVauLbsW3ny6OOFMcJiVu'), -- Password: admin123
                                                  ('user', 'user@example.com', '$2a$10$52YjHvlAZU6nCHLmJBJUeOO4r2.jVWTc5AFFCM7b8OdXCaoj9JNjC'); -- Password: user123

-- Assign Roles to Users
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- Admin user gets ROLE_ADMIN
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- Normal user gets ROLE_USER

-- Insert Sample Products
INSERT INTO products (name, price, quantity) VALUES
                                                 ('Product 1', 10.99, 100),
                                                 ('Product 2', 20.99, 50),
                                                 ('Product 3', 30.99, 75);