package com.example.transformation.outbox.client;

import java.util.List;

public record V2CustomerPayload(
    String externalId,
    String name,
    String email,
    List<V2OrderPayload> orders
) {
}
