CREATE SEQUENCE "public".attempt_id_seq
    START WITH 1;

CREATE SEQUENCE "public".game_id_seq
    START WITH 1;

CREATE SEQUENCE "public".player_id_seq
    START WITH 1;

CREATE TABLE "public".player
(
    id                   serial                                NOT NULL,
    login                text                                  NOT NULL,
    pass                 text                                  NOT NULL,
    date_of_registration timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_player_id PRIMARY KEY (id),
    CONSTRAINT _0 UNIQUE (login)
);

CREATE TABLE "public".game
(
    id             serial                                NOT NULL,
    date_of_game   timestamptz DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player         integer                               NOT NULL,
    guessed_number text                                  NOT NULL,
    CONSTRAINT pk_game_id PRIMARY KEY (id),
    CONSTRAINT fk_game_player FOREIGN KEY (player) REFERENCES "public".player (id) ON DELETE CASCADE
);

CREATE INDEX idx_game_player
    ON "public".game (player);

CREATE TABLE "public".attempt
(
    id           serial                              NOT NULL,
    "number"     text                                NOT NULL,
    game         integer                             NOT NULL,
    attempt_time timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT pk_attempt_id PRIMARY KEY (id),
    CONSTRAINT fk_attempt_game FOREIGN KEY (game) REFERENCES "public".game (id) ON DELETE CASCADE
);

CREATE INDEX idx_attempt_game
    ON "public".attempt (game);