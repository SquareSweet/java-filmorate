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
        return service.getAll();
    }

    @GetMapping("{id}")
    User find(@PathVariable int id) {
        return service.get(id);
    }

    @PostMapping
    User create(@RequestBody User user) {
        return service.create(user);
    }

    @PutMapping
    User update(@RequestBody User user) {
        return service.update(user);
    }

    @PutMapping("{id}/friends/{friendId}")
    User addFriend(@PathVariable int id, @PathVariable int friendId) {
        return service.addFriend(id, friendId);
    }

    @DeleteMapping("{id}/friends/{friendId}")
    User deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        return service.deleteFriend(id, friendId);
    }

    @GetMapping("{id}/friends")
    List<User> getFriends(@PathVariable int id) {
        return service.getFriends(id);
    }

    @GetMapping("{id}/friends/common/{otherId}")
    List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return service.getCommonFriends(id, otherId);
    }
}
