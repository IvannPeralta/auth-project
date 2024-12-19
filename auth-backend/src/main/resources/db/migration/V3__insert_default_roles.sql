CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (username, password)
VALUES ('ADMIN', crypt('admin1234', gen_salt('bf')));
INSERT INTO roles (name) VALUES ('ADMIN');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id AS role_id, p.id AS permission_id
FROM permissions p
JOIN roles r ON r.name = 'ADMIN'
WHERE p.name IN ('PERMISSION_CREATE_USER', 'PERMISSION_DELETE_USER', 'PERMISSION_VIEW_USER', 'PERMISSION_CREATE_ROLE', 'PERMISSION_EDIT_ROLE', 'PERMISSION_DELETE_ROLE');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id AS user_id, r.id AS role_id
FROM users u
JOIN roles r ON r.name = 'ADMIN'
WHERE u.username = 'ADMIN';
