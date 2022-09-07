package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final HashMap<Integer,Film> films = new HashMap<>();

    private int nextId = 1;

    private int getNextId() {
        return nextId++;
    }

    @Override
    public Film create(Film film) {
        if (isValid(film)) {
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Добавлен фильм id={}", film.getId());
            return film;
        } else {
            throw new ValidationException("Некорректно заполнены поля фильма");
        }
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            log.warn("Ошибка при обновлении фильма: фильма с id={} не существует", film.getId());
            throw new ValidationException("Фильма с id=" + film.getId() + " не существует");
        } else if (!isValid(film)) {
            throw new ValidationException("Некорректно заполнены поля фильма");
        } else {
            films.put(film.getId(), film);
            log.info("Обновлён фильм id={}", film.getId());
            return film;
        }
    }

    @Override
    public Film get(int id) {
        log.info("Получен запрос на получение фильма id={}", id);
        return films.get(id);
    }

    @Override
    public List<Film> getAll() {
        log.info("Получен запрос на полный список фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public void delete(int id) {
        films.remove(id);
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
