package ru.job4j.cinema.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

/**
 * Глобальный обработчик исключений
 * @see NoSuchElementException
 * @author Alexander Emelyanov
 * @version 1.0
 */
@Slf4j
@ControllerAdvice
public class NoSuchElementExceptionHandler {

    /**
     * Выполняет глобальный (уровня приложения) перехват исключений
     * NoSuchElementException, в случае перехвата, направляет информацию
     * об исключении на соответствующую веб страницу.
     *
     * @param e перехваченное исключение
     * @return модель для передачи данных об исключении на веб страницу
     */
    @ExceptionHandler(value = {NoSuchElementException.class})
    public ModelAndView handleException(Exception e) {
        log.error(e.getMessage());
        return new ModelAndView("error/404", "errorMessage", e.getMessage());
    }
}
