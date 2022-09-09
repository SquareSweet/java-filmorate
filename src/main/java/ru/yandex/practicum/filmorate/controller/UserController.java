package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {
    UserService service;

    @Autowired
    UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping
    List<User> findAll() {
        log.debug("Получен запрос получение списка всех пользователей");
        return service.getAll();
    }

    @GetMapping("{id}")
    User find(@PathVariable int id) {
        log.debug("Получен запрос на получение пользователя id={}", id);
        return service.get(id);
    }

    @PostMapping
    User create(@RequestBody User user) {
        log.debug("Получен запрос на сохдание пользователя");
        return service.create(user);
    }

    @PutMapping
    User update(@RequestBody User user) {
        log.debug("Получен запрос на обновление пользователя id={}", user.getId());
        return service.update(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    User addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос на добаление пользователя id={} в друзья пользователя id={}", friendId, id);
        return service.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.debug("Получен запрос на удвление пользователя id={} из друзей полтзователя id={}", friendId, id);
        return service.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    List<User> getFriends(@PathVariable int id) {
        log.debug("Получен запрос получение списка друзей пользователя id={}", id);
        return service.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.debug("Получен запрос получение списка общих друзей пользователей id={} и id={}", id, otherId);
        return service.getCommonFriends(id, otherId);
    }
}
