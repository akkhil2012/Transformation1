package com.example.transformation.transformer.dto;

import java.util.List;

public record V3CustomerPayload(
    String externalId,
    String fullName,
    String email,
    String addressLine1,
    String city,
    List<V3OrderPayload> orders
) {
}
