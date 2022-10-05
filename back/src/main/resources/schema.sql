CREATE TABLE IF NOT EXISTS game
(
    id integer NOT NULL PRIMARY KEY,
    player1 text NOT NULL,
    player2 text NOT NULL,
    board integer NOT NULL,
    unique(id)
);


CREATE TABLE IF NOT EXISTS players(
    id SERIAL PRIMARY KEY,
    user TEXT NOT NULL
);
