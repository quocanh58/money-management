package com.quocanhit.moneymanagement.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginateResponseDTO {
    public boolean success;
    public Object data;
    public Object message;
    public String devMessage;
    public Object extraData;
    private Paginate paginate;
}
