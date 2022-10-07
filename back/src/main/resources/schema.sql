CREATE TABLE IF NOT EXISTS players(
    id SERIAL PRIMARY KEY,
    user TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS board
(
    id integer NOT NULL PRIMARY KEY,
    player1 integer NOT NULL,
    player2 integer NOT NULL,
    board integer NOT NULL,
    CONSTRAINT fk1 FOREIGN KEY (player1)
      REFERENCES players (id) ON DELETE CASCADE,
    CONSTRAINT fk2 FOREIGN KEY (player2)
      REFERENCES players (id) ON DELETE CASCADE,
    unique(id)
);



