package upc.ecovolt.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import upc.ecovolt.mapping.dto.ApiResponseDto;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponseDto<Void>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage();
        String status = "ERROR";
        String title = "Error en la Operación";

        // Regla de Negocio SaaS
        if (message.contains("Límite")) {
            status = "WARNING";
            title = "Aviso de Suscripción";
        }

        ApiResponseDto<Void> response = ApiResponseDto.<Void>builder()
                .title(title)
                .message(message)
                .status(status)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}