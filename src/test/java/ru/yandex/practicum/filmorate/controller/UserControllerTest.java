package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void createValid() {
        User expectedUser = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        User actualUser = userController.create(expectedUser);

        assertEquals(expectedUser, actualUser, "Пользователи не совпадают");
    }

    @Test
    void createInvalidEmail() {
        User expectedUser = new User(
                1,
                "testemail.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.create(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void createEmptyEmail() {
        User expectedUser = new User(
                1,
                "",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.create(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void createInvalidLogin() {
        User expectedUser = new User(
                1,
                "test@email.com",
                "test login",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.create(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void createEmptyLogin() {
        User expectedUser = new User(
                1,
                "test@email.com",
                "",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.create(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void createInvalidDate() {
        User expectedUser = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(2990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.create(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateValid() {
        User user = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );
        userController.create(user);

        User expectedUser = new User(
                1,
                "test@email.ru",
                "testLoginUpdated",
                "Test user updated name",
                LocalDate.of(1990, 12, 10)
        );

        User actualUser = userController.update(expectedUser);

        assertEquals(expectedUser, actualUser, "Пользователи не совпадают");
    }

    @Test
    void updateInvalidEmail() {
        User user = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );
        userController.create(user);

        User expectedUser = new User(
                1,
                "testemail.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.update(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateEmptyEmail() {
        User user = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );
        userController.create(user);

        User expectedUser = new User(
                1,
                "",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.update(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateInvalidLogin() {
        User user = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );
        userController.create(user);

        User expectedUser = new User(
                1,
                "test@email.com",
                "test login",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.update(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateEmptyLogin() {
        User user = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );
        userController.create(user);

        User expectedUser = new User(
                1,
                "test@email.com",
                "",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.update(expectedUser),
                "Должно выбрасываться исключение"
        );
    }

    @Test
    void updateInvalidDate() {
        User user = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(1990, 12, 12)
        );
        userController.create(user);

        User expectedUser = new User(
                1,
                "test@email.com",
                "testLogin",
                "Test user name",
                LocalDate.of(2990, 12, 12)
        );

        assertThrows(
                ValidationException.class,
                () -> userController.update(expectedUser),
                "Должно выбрасываться исключение"
        );
    }
}