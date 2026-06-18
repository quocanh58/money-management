package com.quocanhit.moneymanagement.payload.response;

import com.quocanhit.moneymanagement.dto.common.Paginate;
import com.quocanhit.moneymanagement.dto.common.PaginateResponseDTO;
import com.quocanhit.moneymanagement.dto.common.ResponseDTO;
import com.quocanhit.moneymanagement.util.LoggerUtil;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;

public class BaseResponse {
    public static ResponseEntity<?> success(String message, Object data, Object extraData, String errorCode) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(true)
                .message(Objects.isNull(message) ? "Operation completed" : message)
                .data(data)
                .extraData(extraData)
                .errorCode(errorCode)
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    public static ResponseEntity<?> success(String message, Object data, String errorCode) {
        return success(message, data, null, errorCode);
    }

    public static ResponseEntity<?> success(String message, Object data, Object extraData) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(true)
                .message(Objects.isNull(message) ? "Operation completed" : message)
                .data(data)
                .extraData(extraData)
                .errorCode("OPERATION_COMPLETED")
                .build();

        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    public static ResponseEntity<?> success(String message, Object data) {
        return success(message, data, null, "OPERATION_COMPLETED");
    }


    public static ResponseEntity<?> success(String message) {
        return success(message, null, null, "OPERATION_COMPLETED");
    }


    public static ResponseEntity<?> success() {
        return success(null, null, null, "OPERATION_COMPLETED");
    }


    public static ResponseEntity<?> error(String message, String devMessage, HttpStatus status, String errorCode) {
        ResponseDTO responseDTO = ResponseDTO.builder()
                .success(false)
                .message(Objects.isNull(message) ? "An error has occurred" : message)
                .devMessage(Objects.isNull(devMessage) ? "" : devMessage)
                .data(null)
                .errorCode(errorCode)
                .build();

        return new ResponseEntity<>(responseDTO, (status != null) ? status : HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public static ResponseEntity<?> error(String message, HttpStatus status) {
        return error(message, null, status, "OPERATION_COMPLETED");
    }


    public static ResponseEntity<?> error(String message) {
        return error(message, null, HttpStatus.INTERNAL_SERVER_ERROR, "OPERATION_COMPLETED");
    }


    public static ResponseEntity<?> error() {
        return error(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    public static ResponseEntity<?> paginate(Page<?> page, String message, Object extraData) {
        try {
            PaginateResponseDTO responseObject = PaginateResponseDTO.builder()
                    .success(true)
                    .message(Objects.isNull(message) ? "Operation completed" : message)
                    .devMessage("")
                    .data(page.getContent())
                    .extraData(extraData)
                    .paginate(new Paginate(
                            page.getTotalElements(),
                            page.getNumber() + 1,
                            page.getSize(),
                            page.getTotalPages()
                    ))
                    .build();
            return new ResponseEntity<>(responseObject, HttpStatus.OK);
        } catch (Exception e) {
            LoggerUtil.error("Error occurred while processing pagination", e);
            PaginateResponseDTO responseObject = PaginateResponseDTO.builder()
                    .success(false)
                    .message("An error occurred")
                    .devMessage(e.getMessage())
                    .data(Collections.emptyList())
                    .extraData(extraData)
                    .paginate(new Paginate(0, 0, 0, 0))
                    .build();
            return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static ResponseEntity<?> paginate(Page<?> page, String message) {
        return paginate(page, message, null);
    }

    public static ResponseEntity<?> paginate(Page<?> page) {
        return paginate(page, null, null);
    }
}
