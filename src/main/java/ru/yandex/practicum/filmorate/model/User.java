package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;
    final Set<Integer> friends = new HashSet<>();
}
