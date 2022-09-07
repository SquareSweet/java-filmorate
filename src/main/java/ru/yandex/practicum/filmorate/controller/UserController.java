package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    UserStorage userStorage;

    @Autowired
    UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping
    List<User> findAll() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    User find(@PathVariable int id) {
        return userStorage.get(id);
    }

    @PostMapping
    User create(@RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping
    User update(@RequestBody User user) {
        return userStorage.update(user);
    }
}
