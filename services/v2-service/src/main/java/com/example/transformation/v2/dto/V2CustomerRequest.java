package com.example.transformation.v2.dto;

import java.util.List;

public record V2CustomerRequest(
    String externalId,
    String name,
    String email,
    List<V2OrderRequest> orders
) {
}
