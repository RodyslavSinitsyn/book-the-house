package bth.ui.exception;

import bth.models.exception.PostNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    @ExceptionHandler(PostNotFoundException.class)
    public String handlePostNotFoundException(PostNotFoundException exception, Model model) {
        model.addAttribute(ERROR_MESSAGE_KEY, exception.getMessage());
        return ERROR_PAGE;
    }

    @ExceptionHandler(PostServiceException.class)
    public String handlePostServiceException(PostServiceException ex, Model model) {
        model.addAttribute(ERROR_MESSAGE_KEY, ex.getMessage());
        return ERROR_PAGE;
    }
}
