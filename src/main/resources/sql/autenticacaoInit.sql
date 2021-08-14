INSERT INTO roles (name) VALUES ('PRODUTOR');
INSERT INTO roles (name) VALUES ('COMPRADOR');
INSERT INTO roles (name) VALUES ('FUNCIONARIO');
INSERT INTO roles (name) VALUES ('ADMIN');

-- A senha corresponde ao nome de usuario
INSERT INTO users (username, password, enabled) VALUES ('produtor', '$2a$10$dX8SV.x7DlZZ6aiypG/Kn.gHx0.o5X2E5tiEY3qVw7cYR0mAZlwKy', '1');
INSERT INTO users (username, password, enabled) VALUES ('comprador', '$2a$10$Ngpq0A0BPUd96iuQ0ApBMuvZWm74GWgd0TOCc3SyhhFXsiTVktrsW', '1');
INSERT INTO users (username, password, enabled) VALUES ('funcionario', '$2a$10$GTxhuOqydE6vG8rZ2xhXpezta93DNpp3xusUXtx162jvoyQIM8GP6', '1');
INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$Lzef7b873kCdqgdCmKN/M.o9jhwAJm6c861eE7Zb/CNXHAg9DwO6i', '1');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1); -- user produtor has role PRODUTOR
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2); -- user comprador has role COMPRADOR
INSERT INTO users_roles (user_id, role_id) VALUES (3, 3); -- user funcionario has role FUNCIONARIO
INSERT INTO users_roles (user_id, role_id) VALUES (4, 4); -- user admin has role ADMIN
