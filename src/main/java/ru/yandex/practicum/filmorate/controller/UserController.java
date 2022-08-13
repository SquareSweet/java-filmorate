package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
        if (isValid(user)) {
            users.put(user.getId(), user);
            return user;
        } else {
            throw new ValidationException("Некорректно заполнены поля пользователя");
        }
    }

    @PutMapping("/{userId}")
    User update(@PathVariable Integer userId, @RequestBody User user) {
        if (userId == user.getId() || !isValid(user)) {
            users.put(userId, user);
            return user;
        } else {
            throw new ValidationException("Некорректно заполнены поля пользователя");
        }
    }

    private boolean isValid(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            return false;
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            return false;
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            return false;
        }
        return true;
    }
}
