CREATE TABLE IF NOT EXISTS players(
    id serial PRIMARY KEY,
    user_player VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS boards
(
    id serial NOT NULL PRIMARY KEY,
    num_rows integer NOT NULL,
    num_columns integer NOT NULL,
    id_host_player integer NOT NULL,
    id_guest_player integer,
    matriz integer[][] NOT NULL,
    CONSTRAINT FK_board_player1 FOREIGN KEY (id_host_player)
      REFERENCES players (id) ON DELETE SET NULL,
    CONSTRAINT FK_board_player2 FOREIGN KEY (id_guest_player)
      REFERENCES players (id) ON DELETE SET NULL
);



