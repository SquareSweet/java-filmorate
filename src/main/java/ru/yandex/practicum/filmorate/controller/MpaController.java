package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.List;

@RestController
@RequestMapping("mpa")
@Slf4j
public class MpaController {
    private final MpaStorage storage;

    @Autowired
    MpaController(MpaStorage mpaStorage) {
        this.storage = mpaStorage;
    }

    @GetMapping
    List<Mpa> findAll() {
        log.debug("Получен запрос на полный список рейтингов");
        return storage.getAll();
    }

    @GetMapping("{id}")
    Mpa find(@PathVariable int id) {
        log.debug("Получен запрос на рейтинг id={}", id);
        return storage.get(id);
    }
}
