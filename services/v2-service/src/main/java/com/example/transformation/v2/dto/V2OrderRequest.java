package com.example.transformation.v2.dto;

import java.math.BigDecimal;

public record V2OrderRequest(
    String orderNumber,
    BigDecimal total
) {
}
