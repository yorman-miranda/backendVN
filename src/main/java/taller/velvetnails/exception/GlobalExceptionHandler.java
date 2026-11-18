package taller.velvetnails.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import taller.velvetnails.dto.ErrorResponseDTO;

import java.time.DateTimeException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ){
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage() + " no existe",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ){
        String mensajes = ex.getAllErrors()
                .stream()
                .map(error -> mensajeDeValidacion(error))
                .filter(message -> !message.isBlank())
                .collect(Collectors.joining("; "));

        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                mensajes.isBlank() ? "La solicitud contiene datos invalidos." : mensajes,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private String mensajeDeValidacion(ObjectError error) {
        String mensaje = error.getDefaultMessage();
        if (error instanceof FieldError fieldError) {
            return fieldError.getField() + " " + (mensaje == null ? "" : mensaje);
        }
        return error.getObjectName() + " " + (mensaje == null ? "" : mensaje);
    }

    @ExceptionHandler({fechaIvalida.class, DateTimeException.class})
    public ResponseEntity<ErrorResponseDTO> handleInvalidDate(
            Exception ex,
            HttpServletRequest request
    ){
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                "La fecha indicada no es valida.",
                request.getRequestURI()
        );

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ExistClienteByDocument.class)
    public ResponseEntity<ErrorResponseDTO> handleExistClienteByDocument(
            ExistClienteByDocument ex,
            HttpServletRequest request
    ){
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "El documento ya existe; no se puede repetir.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ExistFechaAndHora.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidAppointmentTime(
            ExistFechaAndHora ex,
            HttpServletRequest request
    ){
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ModificacionCitaFueraDePlazoException.class)
    public ResponseEntity<ErrorResponseDTO> handleModificationOutsideWindow(
            ModificacionCitaFueraDePlazoException ex,
            HttpServletRequest request
    ){
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolation(
            ConstraintViolationException ex,
            HttpServletRequest request
    ){
        String message = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + " " + violation.getMessage())
                .collect(Collectors.joining("; "));
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                message.isBlank() ? "La solicitud contiene datos invalidos." : message,
                request.getRequestURI()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataConflict(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ){
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.CONFLICT.value(),
                "Conflict",
                "La operacion entra en conflicto con datos existentes.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(
            Exception ex,
            HttpServletRequest request
    ){
        if (ex instanceof ErrorResponse errorResponse) {
            HttpStatusCode status = errorResponse.getStatusCode();
            ErrorResponseDTO error = new ErrorResponseDTO(
                    status.value(),
                    status.is4xxClientError() ? "Solicitud invalida" : "Error",
                    status.is4xxClientError()
                            ? "La solicitud no pudo procesarse."
                            : "Ha ocurrido un error inesperado.",
                    request.getRequestURI()
            );
            return ResponseEntity.status(status).body(error);
        }

        LOGGER.log(Level.SEVERE, "Error inesperado en " + request.getRequestURI(), ex);
        ErrorResponseDTO error = new ErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ha ocurrido un error inesperado.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
