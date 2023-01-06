package ru.job4j.cinema.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

//    private final ObjectMapper objectMapper;
//
//    public GlobalExceptionHandler(ObjectMapper objectMapper) {
//        this.objectMapper = objectMapper;
//    }

//    @ExceptionHandler(value = {NoSuchElementException.class})
//    public void handleException(Exception e, HttpServletRequest request,
//                                HttpServletResponse response) throws IOException {
//        response.setStatus(HttpStatus.BAD_REQUEST.value());
//        response.setContentType("application/json");
//        response.getWriter().write(objectMapper.writeValueAsString(new HashMap<>() { {
//            put("message", "Some of fields empty");
//            put("details", e.getMessage());
//        }}));
//        log.error(e.getMessage());
//    }

    @ExceptionHandler(value = {NoSuchElementException.class})
    public ModelAndView handleException(Exception e) {
        log.error(e.getMessage());
        return new ModelAndView("error/404", "errorMessage", e.getMessage());
    }
}
