package com.accompany.order.service.foot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FootCountOnMonth {
    private int count;
    private String footName;
    private Long footId;
}
