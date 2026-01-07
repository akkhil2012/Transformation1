package com.example.transformation.transformer.dto;

import java.math.BigDecimal;

public record V2OrderPayload(
    String orderNumber,
    BigDecimal total
) {
}
