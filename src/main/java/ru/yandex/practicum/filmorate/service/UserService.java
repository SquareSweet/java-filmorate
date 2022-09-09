package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.storage = userStorage;
    }

    public User create(User user) {
        return storage.create(user);
    }

    public User update(User user) {
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
            friend.getFriends().add(userId);
            storage.update(friend);
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

            if (friend.getFriends().contains(userId)) {
                friend.getFriends().remove(userId);
            } else {
                throw new NotFoundException("Пользователя id=" + friendId + " нет в дрезьях у пользователя id=" + userId);
            }
            storage.update(friend);
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
        User user1 = storage.get(id);
        User user2 = storage.get(userId);
        Set<Integer> commonIds = user1.getFriends();
        commonIds.retainAll(user2.getFriends());
        List<User> friends = new ArrayList<>();
        for (int friendId : commonIds) {
            friends.add(storage.get(friendId));
        }
        return friends;
    }
}
