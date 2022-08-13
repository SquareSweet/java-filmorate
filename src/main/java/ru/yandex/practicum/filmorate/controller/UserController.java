package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    private final HashMap<Integer,User> users = new HashMap<>();

    @GetMapping
    List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    User create(@RequestBody User user) {
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping("/{userId}")
    User update(@PathVariable Integer userId, @RequestBody User user) {
        if (userId == user.getId()) {
            users.put(userId, user);
            return user;
        } else {
            throw new ValidationException("Не совпадают id пользователя");
        }
    }
}
