package ru.job4j.cinema.model;

import lombok.*;

/**
 * Модель данных пользователи
 * @author Alexander Emelyanov
 * @version 1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    /**
     * Идентификатор пользователя
     */
    @EqualsAndHashCode.Include
    private int id;

    /**
     * Имя пользователя
     */
    private String username;

    /**
     * Электронный адрес пользователя
     */
    private String email;

    /**
     * Телефонный номер пользователя
     */
    private String phone;

    /**
     * Пароль пользователя
     */
    private String password;
}
