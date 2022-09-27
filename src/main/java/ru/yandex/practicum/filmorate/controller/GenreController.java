package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.util.List;

@RestController
@RequestMapping("genres")
@Slf4j
public class GenreController {
    private final GenreStorage storage;

    @Autowired
    GenreController(GenreStorage genreStorage) {
        this.storage = genreStorage;
    }

    @GetMapping
    List<Genre> findAll() {
        log.debug("Получен запрос на полный список фильмов");
        return storage.getAll();
    }

    @GetMapping("{id}")
    Genre find(@PathVariable int id) {
        log.debug("Получен запрос на жанр id={}", id);
        return storage.get(id);
    }
}
