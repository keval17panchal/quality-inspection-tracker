CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default admin user (admin / admin)
INSERT INTO users (username, password, name, role)
SELECT 'admin', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVymGe07xd00DMxs.AQubh4a', 'System Administrator', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

-- Insert default supervisor user (supervisor / supervisor)
INSERT INTO users (username, password, name, role)
SELECT 'supervisor', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Plant Supervisor', 'SUPERVISOR'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'supervisor');

-- Insert default inspector viewer user (inspector / inspector)
INSERT INTO users (username, password, name, role)
SELECT 'inspector', '$2a$10$8.UnVuG9HHgffUDAlk8qfOUVGkqRzgVymGe07xd00DMxs.AQubh4a', 'Quality Inspector', 'INSPECTOR'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'inspector');
