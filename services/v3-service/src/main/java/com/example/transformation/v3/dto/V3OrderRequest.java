package com.example.transformation.v3.dto;

import java.math.BigDecimal;

public record V3OrderRequest(
    String orderNumber,
    BigDecimal total
) {
}
