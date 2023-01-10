package ru.job4j.cinema.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class IllegalArgumentExceptionHandler {

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ModelAndView handleException(Exception e) {
        log.error(e.getMessage());
        return new ModelAndView("error/400", "errorMessage", e.getMessage());
    }
}
