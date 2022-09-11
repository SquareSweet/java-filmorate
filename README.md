# java-filmorate
![Схема базы данных](https://github.com/SquareSweet/java-filmorate/blob/main/extra/db-scheme.png)

Примеры запросов:

Выгрузука списка 10 самых популярных фильмов
```sql
SELECT
  film.id,
  COUNT(film_likes.user_id) as likes_count
FROM
  film
  LEFT JOIN film_likes on film.id = film_likes.film_id
GROUP BY
  film.id
ORDER BY
  likes_count DESC
LIMIT 10
```

Выгрузка списка общих друзей пользователей с id=1 и id=2
```sql
SELECT
  mutual_info.*
FROM
  user_friend one
  INNER JOIN user_friend mutual on mutual.user_id = one.friend_id
  INNER JOIN user_friend two on two.user_id = mutual.friend_id
  INNER JOIN user mutual_info on mutual.user_id = mutual_info.id
WHERE
  one.user_id = 1
  AND two.user_id = 2
  AND one.accepted = true
  AND mutual.accepted = true
```
