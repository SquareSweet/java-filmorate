package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.storage = filmStorage;
    }

    public Film create(Film film) {
        return storage.create(film);
    }

    public Film update(Film film) {
        return storage.update(film);
    }

    public Film get(int id) {
        return storage.get(id);
    }

    public List<Film> getAll() {
        return storage.getAll();
    }

    public Film putLike(int filmId, int userId) {
        Film film = storage.get(filmId);
        film.getLikes().add(userId);
        storage.update(film);
        return film;
    }

    public Film deleteLike(int filmId, int userId) {
        Film film = storage.get(filmId);
        if (film.getLikes().contains(userId)) {
            film.getLikes().remove(userId);
        } else {
            throw new NotFoundException("На фильме id=" + filmId + " нет лайка от пользователя id=" + userId);
        }
        storage.update(film);
        return film;
    }

    public List<Film> getPopular(int count) {
        return storage.getAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getLikes().size(), o1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
