package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage storage;

    @Autowired
    public FilmService(@Qualifier("dbStorage")FilmStorage filmStorage) {
        this.storage = filmStorage;
    }

    public Film create(Film film) {
        if(!isValid(film)) throw new ValidationException("Некорректно заполнены поля фильма");
        return storage.create(film);
    }

    public Film update(Film film) {
        if(!isValid(film)) throw new ValidationException("Некорректно заполнены поля фильма");
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

    private boolean isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: пустое название", film.getId());
            return false;
        }
        if (film.getDescription() == null || film.getDescription().length() > 200) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: длина описания больше 200 символов", film.getId());
            return false;
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(LocalDate.of(1895,12,28))) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: некорректная дата релиза", film.getId());
            return false;
        }
        if (film.getDuration() < 0) {
            log.warn("Ошибка при добавлении/обновлении фильма id={}: некорректная продолжительность", film.getId());
            return false;
        }
        return true;
    }
}
