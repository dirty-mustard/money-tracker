package moneytracker.controllers;

import moneytracker.exceptions.NotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice(annotations = RestController.class)
public class ExceptionListener {

    private static final Logger LOG = Logger.getLogger("MoneyTracker Logger");

    private static final String ERROR = "error";
    private static final String ERROR_DETAILS = "errorDetails";

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(Exception ex) {

        LOG.debug(ex.getMessage(), ex);

        return new HashMap<String, String>() {{
            put(ERROR, ex.getMessage());
        }};

    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMissingServletRequestParameter(MissingServletRequestParameterException ex) {

        LOG.debug(ex.getMessage(), ex);

        return new HashMap<String, String>() {{
            put(ERROR, ex.getMessage());
        }};

    }

    @ExceptionHandler(TypeMismatchException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleTypeMismatch(TypeMismatchException ex) {

        LOG.debug(ex.getMessage(), ex);

        Throwable rootCause = ex.getRootCause();
        String message = (rootCause != null) ? rootCause.getMessage() : ex.getMessage();

        return new HashMap<String, String>() {{
            put(ERROR, message);
        }};

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {

        LOG.debug(ex.getMessage(), ex);

        Throwable rootCause = ex.getRootCause();
        String message = (rootCause != null) ? rootCause.getMessage() : "HTTP message not readable";

        return new HashMap<String, String>() {{
            put(ERROR, message);
        }};

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {

        LOG.debug(ex.getMessage(), ex);

        return new HashMap<String, Object>() {{
            put(ERROR, "Validation failed, please check error details");
            put(ERROR_DETAILS, getErrorDetails(ex.getBindingResult()));
        }};

    }

    private Map<String, String> getErrorDetails(BindingResult result) {

        Map<String, String> errorDetails = new HashMap<>();
        result.getFieldErrors().forEach(
            fieldError -> errorDetails.put(
                fieldError.getField(),
                fieldError.getDefaultMessage()
            )
        );

        return errorDetails;

    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUnknown(Exception ex) {

        LOG.error(ex.getMessage(), ex);

        return new HashMap<String, String>() {{
            put(ERROR, "Internal server error");
        }};

    }

}
