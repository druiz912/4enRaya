CREATE TABLE jugadores (
  id INTEGER PRIMARY KEY,
  userPlayer VARCHAR(255) NOT NULL
);

CREATE TABLE fichas (
  id INTEGER PRIMARY KEY,
  jugador_id INTEGER,
  fila INTEGER,
  columna INTEGER,
  disponible BOOLEAN,
  FOREIGN KEY (jugador_id) REFERENCES jugadores(id)
);

CREATE TABLE tableros (
  id INTEGER PRIMARY KEY AUTO_INCREMENT,
  filas INTEGER,
  columnas INTEGER,
  status VARCHAR(255)
);

CREATE TABLE tableros_jugadores (
  tablero_id INTEGER,
  jugador_id INTEGER,
  PRIMARY KEY (tablero_id, jugador_id),
  FOREIGN KEY (tablero_id) REFERENCES tableros(id),
  FOREIGN KEY (jugador_id) REFERENCES jugadores(id)
);

CREATE TABLE tableros_fichas (
  tablero_id INTEGER,
  ficha_id INTEGER,
  PRIMARY KEY (tablero_id, ficha_id),
  FOREIGN KEY (tablero_id) REFERENCES tableros(id),
  FOREIGN KEY (ficha_id) REFERENCES fichas(id)
);

CREATE TABLE users (
  id VARCHAR(255) PRIMARY KEY,
  username VARCHAR(255),
  password VARCHAR(255),
);

CREATE TABLE roles (
  id VARCHAR(255) PRIMARY KEY,
  name VARCHAR(255)
);

CREATE TABLE users_roles (
  user_id VARCHAR(255),
  role_id VARCHAR(255),
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (role_id) REFERENCES roles(id)
);
