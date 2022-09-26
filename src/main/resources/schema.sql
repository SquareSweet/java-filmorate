DROP TABLE IF EXISTS user_friend;
DROP TABLE IF EXISTS film_genre;
DROP TABLE IF EXISTS film_likes;
DROP TABLE IF EXISTS genres;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS films;
DROP TABLE IF EXISTS ratings;

CREATE TABLE IF NOT EXISTS users
(
    id       int PRIMARY KEY AUTO_INCREMENT,
    email    varchar(50) NOT NULL,
    login    varchar(50) NOT NULL,
    name     varchar,
    birthday date
);

CREATE TABLE IF NOT EXISTS user_friend
(
    user_id   int     NOT NULL,
    friend_id int     NOT NULL
);

CREATE TABLE IF NOT EXISTS films
(
    id           int PRIMARY KEY AUTO_INCREMENT,
    name         varchar(100) NOT NULL,
    description  varchar(200) NOT NULL,
    rating       int NOT NULL,
    release_date date,
    duration     int
);

CREATE TABLE IF NOT EXISTS genres
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS film_genre
(
    film_id  int NOT NULL,
    genre_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS film_likes
(
    film_id int NOT NULL,
    user_id int NOT NULL
);

CREATE TABLE IF NOT EXISTS ratings
(
    id   int PRIMARY KEY AUTO_INCREMENT,
    name varchar(10) NOT NULL
);

ALTER TABLE user_friend
    ADD CONSTRAINT IF NOT EXISTS fk_user_friend_user_id FOREIGN KEY (user_id)
        REFERENCES users (id);

ALTER TABLE user_friend
    ADD CONSTRAINT IF NOT EXISTS fk_user_friend_friend_id FOREIGN KEY (friend_id)
        REFERENCES users (id);

ALTER TABLE films
    ADD CONSTRAINT IF NOT EXISTS fk_ratings FOREIGN KEY (rating)
        REFERENCES ratings (id);

ALTER TABLE film_genre
    ADD CONSTRAINT IF NOT EXISTS fk_film_genre_film_id FOREIGN KEY (film_id)
        REFERENCES films (id);

ALTER TABLE film_genre
    ADD CONSTRAINT IF NOT EXISTS fk_film_genre_genre_id FOREIGN KEY (genre_id)
        REFERENCES genres (id);

ALTER TABLE film_likes
    ADD CONSTRAINT IF NOT EXISTS fk_film_likes_film_id FOREIGN KEY (film_id)
        REFERENCES films (id);

ALTER TABLE film_likes
    ADD CONSTRAINT IF NOT EXISTS fk_film_likes_user_id FOREIGN KEY (user_id)
        REFERENCES users (id);