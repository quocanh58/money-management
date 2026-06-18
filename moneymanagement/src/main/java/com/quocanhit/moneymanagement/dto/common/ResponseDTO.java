package com.quocanhit.moneymanagement.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseDTO {
    public boolean success ;
    public Object data;
    public Object message;
    public String devMessage;
    public Object extraData;
    public String errorCode;
}

