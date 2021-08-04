INSERT INTO roles (name) VALUES ('PRODUTOR');
INSERT INTO roles (name) VALUES ('COMPRADOR');
INSERT INTO roles (name) VALUES ('FUNCIONARIO');
INSERT INTO roles (name) VALUES ('ADMIN');

INSERT INTO users (username, password, enabled) VALUES ('patrick', '$2a$10$cTUErxQqYVyU2qmQGIktpup5chLEdhD2zpzNEyYqmxrHHJbSNDOG.', '1');
INSERT INTO users (username, password, enabled) VALUES ('alex', '$2a$10$.tP2OH3dEG0zms7vek4ated5AiQ.EGkncii0OpCcGq4bckS9NOULu', '1');
INSERT INTO users (username, password, enabled) VALUES ('john', '$2a$10$E2UPv7arXmp3q0LzVzCBNeb4B4AtbTAGjkefVDnSztOwE7Gix6kea', '1');
INSERT INTO users (username, password, enabled) VALUES ('namhm', '$2a$10$GQT8bfLMaLYwlyUysnGwDu6HMB5G.tin5MKT/uduv2Nez0.DmhnOq', '1');
INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$IqTJTjn39IU5.7sSCDQxzu3xug6z/LPU6IF0azE/8CkHCwYEnwBX.', '1');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1); -- user patrick has role USER
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2); -- user alex has role CREATOR
INSERT INTO users_roles (user_id, role_id) VALUES (3, 3); -- user john has role EDITOR
INSERT INTO users_roles (user_id, role_id) VALUES (4, 2); -- user namhm has role CREATOR
INSERT INTO users_roles (user_id, role_id) VALUES (4, 3); -- user namhm has role EDITOR
INSERT INTO users_roles (user_id, role_id) VALUES (5, 4); -- user admin has role ADMIN