package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final HashMap<Integer,User> users = new HashMap<>();
    private int nextId = 1;

    private int getNextId() {
        return nextId++;
    }

    @Override
    public User create(User user) {
        if (isValid(user)) {
            user.setId(getNextId());
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Добавлен пользователь id={}", user.getId());
            return user;
        } else {
            throw new ValidationException("Некорректно заполнены поля пользователя");
        }
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Пользователя с id=" + user.getId() + " не существует");
        } else if (!isValid(user)) {
            throw new ValidationException("Некорректно заполнены поля пользователя");
        } else {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
            log.info("Обновлён пользователь id={}", user.getId());
            return user;
        }
    }

    @Override
    public User get(int id) {
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            throw new NotFoundException("Пользователя id=" + id + " не существует");
        }
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public void delete(int id) {
        users.remove(id);
    }

    private boolean isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Ошибка при добавлении/обновлении пользователя id={}: некорректный email", user.getId());
            return false;
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Ошибка при добавлении/обновлении пользователя id={}: некорректный логин", user.getId());
            return false;
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка при добавлении/обновлении пользователя id={}: некорректная дата", user.getId());
            return false;
        }
        return true;
    }
}
