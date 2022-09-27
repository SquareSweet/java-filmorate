package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(@Qualifier("dbStorage") UserStorage userStorage) {
        this.storage = userStorage;
    }

    public User create(User user) {
        if (!isValid(user)) throw new ValidationException("Некорректно заполнены поля пользователя");
        return storage.create(user);
    }

    public User update(User user) {
        if (!isValid(user)) throw new ValidationException("Некорректно заполнены поля пользователя");
        return storage.update(user);
    }

    public User get(int id) {
        return storage.get(id);
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public User addFriend(int userId, int friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        if (user != null && friend != null) {
            user.getFriends().add(friendId);
            storage.update(user);
            return user;
        } else {
            throw new NotFoundException("Пользователя не существует");
        }
    }

    public User deleteFriend(int userId, int friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        if (user != null && friend != null) {
            if (user.getFriends().contains(friendId)) {
                user.getFriends().remove(friendId);
            } else {
                throw new NotFoundException("Пользователя id=" + friendId + " нет в дрезьях у пользователя id=" + userId);
            }
            storage.update(user);
            return user;
        } else {
            throw new NotFoundException("Пользователя не существует");
        }
    }

    public List<User> getFriends(int id) {
        User user = storage.get(id);
        List<User> friends = new ArrayList<>();
        for (int friendId : user.getFriends()) {
            friends.add(storage.get(friendId));
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int userId) {
        Set<Integer> commonIds = new HashSet<>(storage.get(id).getFriends());
        commonIds.retainAll(storage.get(userId).getFriends());
        List<User> friends = new ArrayList<>();
        for (int friendId : commonIds) {
            friends.add(storage.get(friendId));
        }
        return friends;
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
