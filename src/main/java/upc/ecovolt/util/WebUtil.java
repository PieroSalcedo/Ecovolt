package upc.ecovolt.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import upc.ecovolt.mapping.dto.ApiResponseDto;

public class WebUtil {
    // Para respuestas 200 OK (Listados, búsquedas)
    public static <T> ResponseEntity<ApiResponseDto<T>> ok(T data, String message) {
        return ResponseEntity.ok(ApiResponseDto.<T>builder()
                .title("Operación Exitosa")
                .message(message)
                .status("SUCCESS")
                .data(data)
                .build());
    }

    // Para respuestas 201 Created (Registros nuevos)
    public static <T> ResponseEntity<ApiResponseDto<T>> created(T data, String message) {
        return new ResponseEntity<>(ApiResponseDto.<T>builder()
                .title("¡Registro Exitoso!")
                .message(message)
                .status("SUCCESS")
                .data(data)
                .build(), HttpStatus.CREATED);
    }
}