package ru.job4j.cinema.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Глобальный обработчик исключений
 * @see IllegalArgumentException
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Slf4j
@ControllerAdvice
public class IllegalArgumentExceptionHandler {

    /**
     * Выполняет глобальный (уровня приложения) перехват исключений
     * IllegalArgumentException, в случае перехвата, направляет информацию
     * об исключении на соответствующую веб страницу.
     *
     * @param e перехваченное исключение
     * @return модель для передачи данных об исключении на веб страницу
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ModelAndView handleException(Exception e) {
        log.error(e.getMessage());
        return new ModelAndView("error/400", "errorMessage", e.getMessage());
    }
}
