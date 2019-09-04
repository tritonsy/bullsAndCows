# Описание игры.
Компьютер загадывает 4-х значное число. Цифры загаданного числа не повторяются. Задача пользователя угадать загаданное число. У пользователя неограниченное число попыток. В каждую попытку пользователь дает компьютеру свой вариант числа. Компьютер сообщает сколько цифр точно угадано (бык) и сколько цифр угадано без учета позиции (корова). По ответу компьютера пользователь должен за несколько ходов угадать число.


Пример:  
7328 -- wanted number 

| Input:        | Server answer:     |
| ------------- |:------------------:|
| 0819          | 0B1C               |
| 4073          | 0B1C               |
| 5820          | 0B1C               |
| 3429          | 1B1C               |
| 5960          | 0B0C               |
| 7238          | 2B2C               |
| 7328          | Victory!           |

> [Более подробно об игре](https://ru.wikipedia.org/wiki/Быки_и_коровы)
# Функциональные требования.
- [x] Пользователь может регистрироваться в системе.
- [x] Пользователь может видеть свои предыдущие попытки.
- [x] Компьютер ведет рейтинг пользователей (показатель -- среднее число попыток до угадывания числа).
- [x] Пользователь может выбирать число визуально, с помощью мыши.
# Технические требования и использование технологий:
- [x] [Spring MVC](https://habr.com/ru/post/336816/), [Spring Data](https://habr.com/ru/post/435114/), [Spring Security](https://habr.com/ru/post/203318/), [Hibernate ORM](https://habr.com/ru/post/29694/). Для работы с базой использован JPA. 
- [x] СУБД PostgreSQL. 
- [x] Обмен между фронтом и бэком ведётся через JSON формат.
