package ru.job4j.cinema.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

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
    @NotBlank(message = "Поле не должно быть пустым")
    @Size(min = 5, message = "Имя пользователя должно быть не менее 5 символов")
    private String username;

    /**
     * Электронный адрес пользователя
     */
    @NotBlank(message = "Поле не должно быть пустым")
    private String email;

    /**
     * Телефонный номер пользователя
     */
    @NotBlank(message = "Поле не должно быть пустым")
    @Pattern(regexp = "\\+\\d{11}",
            message = "Должно быть в формате \"+\" и 11 значный номер")
    private String phone;

    /**
     * Пароль пользователя
     */
    private String password;
}
