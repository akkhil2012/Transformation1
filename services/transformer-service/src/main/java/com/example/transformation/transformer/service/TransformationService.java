package com.example.transformation.transformer.service;

import com.example.transformation.transformer.dto.V2CustomerPayload;
import com.example.transformation.transformer.dto.V2OrderPayload;
import com.example.transformation.transformer.dto.V3CustomerPayload;
import com.example.transformation.transformer.dto.V3OrderPayload;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class TransformationService {
  public V3CustomerPayload transform(V2CustomerPayload payload) {
    List<V3OrderPayload> orders = payload.orders() == null ? List.of() : payload.orders()
        .stream()
        .map(this::toV3Order)
        .collect(Collectors.toList());

    return new V3CustomerPayload(
        payload.externalId(),
        payload.name(),
        payload.email(),
        "",
        "",
        orders
    );
  }

  private V3OrderPayload toV3Order(V2OrderPayload order) {
    return new V3OrderPayload(order.orderNumber(), order.total());
  }
}
