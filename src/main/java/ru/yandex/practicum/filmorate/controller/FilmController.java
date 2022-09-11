package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("films")
public class FilmController {

    private final FilmService service;

    @Autowired
    public FilmController(FilmService filmService) {
        this.service = filmService;
    }

    @GetMapping
    List<Film> findAll() {
        log.debug("Получен запрос на полный список фильмов");
        return service.getAll();
    }

    @GetMapping("{id}")
    Film find(@PathVariable int id) {
        log.debug("Получен запрос на фильм id={}", id);
        return service.get(id);
    }

    @PostMapping
    Film create(@RequestBody Film film) {
        log.debug("Получен запрос на добавление фильма");
        return service.create(film);
    }

    @PutMapping
    Film update(@RequestBody Film film) {
        log.debug("Получен запрос на обновление фильма id={}", film.getId());
        return service.update(film);
    }

    @PutMapping("{id}/like/{userId}")
    Film putLike(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен запрос на лайк к фильму id={} от пользователя id={}", id, userId);
        return service.putLike(id, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.debug("Получен запрос на удаления лайка к фильму id={} от пользователя id={}", id, userId);
        return service.deleteLike(id, userId);
    }

    @GetMapping("popular")
    List<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.debug("Получен запрос получение {} самых популярных фильмов", count);
        return service.getPopular(count);
    }
}
