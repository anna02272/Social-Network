INSERT IGNORE INTO users (id, username, password, email, first_name, last_name, type)
VALUES (2, 'user', '$2a$10$B6iM.SdYtQ0xuBeIlMhxUuNarrAwltbJdPRl033nJO4foI4DXQ9V6', 'user@gmail.com', 'User', 'User', 0);

INSERT IGNORE INTO users (id, username, password, email, first_name, last_name, type)
VALUES (1, 'admin', '$2a$10$Ze.8jplfxpne7Jefbw0Tuu9j.M0ez7HepKTCBGLYMVtHklF.VC5zi', 'admin@gmail.com', 'Admin', 'Admin', 1);
