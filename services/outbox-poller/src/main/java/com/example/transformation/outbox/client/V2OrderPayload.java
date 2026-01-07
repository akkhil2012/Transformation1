package com.example.transformation.outbox.client;

import java.math.BigDecimal;

public record V2OrderPayload(
    String orderNumber,
    BigDecimal total
) {
}
