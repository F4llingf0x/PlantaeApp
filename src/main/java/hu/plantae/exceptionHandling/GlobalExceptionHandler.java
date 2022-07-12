package hu.plantae.exceptionHandling;

import hu.plantae.dto.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(MethodArgumentNotValidException exception) {
        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ValidationError>> handleConstrainValidationException(ConstraintViolationException exception) {
        List<ValidationError> validationErrors = exception.getConstraintViolations().stream()
                .map(fieldError -> new ValidationError("Parameter", fieldError.getMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(PlantNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handlePlantNotFound(PlantNotFoundException exception) {
        ValidationError validationError = new ValidationError("PlantId",
                "plant with id: " + exception.getIdNotFound() + " is not found");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SensorNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleSensorNotFound(SensorNotFoundException exception) {
        ValidationError validationError = new ValidationError("SensorId",
                "sensor with id: " + exception.getIdNotFound() + " is not found");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReadingNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleReadingNotFound(ReadingNotFoundException exception) {
        ValidationError validationError = new ValidationError("Reading",
                "Reading is not found");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SwappedLimitParametersException.class)
    public ResponseEntity<List<ValidationError>>swappedLimitParametersException(SwappedLimitParametersException exception) {
        ValidationError validationError = new ValidationError("Parameters",
                "Parameters of " + exception.getParameter() + " are incorrect input");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.BAD_REQUEST);
    }


}