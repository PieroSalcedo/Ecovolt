package upc.ecovolt.mapping.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseDto<T> {
    private String title;
    private String message;
    private String status; // SUCCESS, WARNING, ERROR
    private T data;        // Aquí viajará el objeto (Device, Home, etc.)
}