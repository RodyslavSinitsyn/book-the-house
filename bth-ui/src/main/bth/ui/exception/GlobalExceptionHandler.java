package bth.ui.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String ERROR_MESSAGE_KEY = "errorMessage";
    public static final String ERROR_PAGE = "error/error";

    // Handle specific 404 errors
    @ExceptionHandler(NoHandlerFoundException.class)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        model.addAttribute(ERROR_MESSAGE_KEY, "The resource you are looking for was not found.");
        return ERROR_PAGE;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNotFound(NoSuchElementException exception, Model model) {
        model.addAttribute(ERROR_MESSAGE_KEY, exception.getMessage());
        return ERROR_PAGE;
    }
}
