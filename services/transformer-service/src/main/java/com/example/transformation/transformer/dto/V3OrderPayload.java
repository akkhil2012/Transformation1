package com.example.transformation.transformer.dto;

import java.math.BigDecimal;

public record V3OrderPayload(
    String orderNumber,
    BigDecimal total
) {
}
