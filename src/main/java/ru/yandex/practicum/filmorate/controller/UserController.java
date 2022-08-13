package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("users")
public class UserController {

    private final HashMap<Integer,User> users = new HashMap<>();

    @GetMapping
    List<User> findAll() {
        log.info("Получен запрос на полный список пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping
    User create(@RequestBody User user) {
        if (isValid(user)) {
            users.put(user.getId(), user);
            log.info("Добавлен пользователь id={}", user.getId());
            return user;
        } else {
            throw new ValidationException("Некорректно заполнены поля пользователя");
        }
    }

    @PutMapping("/{userId}")
    User update(@PathVariable Integer userId, @RequestBody User user) {
        if (userId != user.getId()) {
            log.warn("Ошибка при обновлении пользователя: не совпадают id");
            throw new ValidationException("Не совпадают id пользователя");
        } else if (!isValid(user)) {
            throw new ValidationException("Некорректно заполнены поля пользователя");
        } else {
            users.put(userId, user);
            log.info("Обновлён пользователь id={}", userId);
            return user;
        }
    }

    private boolean isValid(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка при добавлении/обновлении пользователя id={}: некорректный email", user.getId());
            return false;
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка при добавлении/обновлении пользователя id={}: некорректный логин", user.getId());
            return false;
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка при добавлении/обновлении пользователя id={}: некорректная дата", user.getId());
            return false;
        }
        return true;
    }
}
