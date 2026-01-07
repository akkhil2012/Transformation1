package com.example.transformation.outbox.client;

import java.math.BigDecimal;

public record V3OrderPayload(
    String orderNumber,
    BigDecimal total
) {
}
