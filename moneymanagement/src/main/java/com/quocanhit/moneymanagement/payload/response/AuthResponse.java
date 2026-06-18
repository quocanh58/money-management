package com.quocanhit.moneymanagement.payload.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AuthResponse<T> {
    private HttpStatus status;
    private String message;
    private T data;

    public AuthResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}
