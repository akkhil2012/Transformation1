package com.example.transformation.v3.dto;

import java.util.List;

public record V3CustomerRequest(
    String externalId,
    String fullName,
    String email,
    String addressLine1,
    String city,
    List<V3OrderRequest> orders
) {
}
