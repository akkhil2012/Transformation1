package com.example.transformation.v2.service;

import com.example.transformation.v2.dto.V2CustomerRequest;
import com.example.transformation.v2.dto.V2OrderRequest;
import com.example.transformation.v2.entity.CustomerV2;
import com.example.transformation.v2.entity.OrderV2;
import com.example.transformation.v2.entity.OutboxTable1;
import com.example.transformation.v2.entity.TransformationRecord;
import com.example.transformation.v2.repository.CustomerV2Repository;
import com.example.transformation.v2.repository.OutboxTable1Repository;
import com.example.transformation.v2.repository.TransformationRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class V2CustomerService {
  private final CustomerV2Repository customerRepository;
  private final OutboxTable1Repository outboxRepository;
  private final TransformationRecordRepository transformationRepository;
  private final ObjectMapper objectMapper;

  public V2CustomerService(
      CustomerV2Repository customerRepository,
      OutboxTable1Repository outboxRepository,
      TransformationRecordRepository transformationRepository,
      ObjectMapper objectMapper
  ) {
    this.customerRepository = customerRepository;
    this.outboxRepository = outboxRepository;
    this.transformationRepository = transformationRepository;
    this.objectMapper = objectMapper;
  }

  @Transactional
  public CustomerV2 createCustomer(V2CustomerRequest request) {
    CustomerV2 customer = new CustomerV2();
    customer.setExternalId(request.externalId());
    customer.setName(request.name());
    customer.setEmail(request.email());

    if (request.orders() != null) {
      for (V2OrderRequest orderRequest : request.orders()) {
        OrderV2 order = new OrderV2();
        order.setOrderNumber(orderRequest.orderNumber());
        order.setTotal(orderRequest.total());
        customer.addOrder(order);
      }
    }

    CustomerV2 savedCustomer = customerRepository.save(customer);

    OutboxTable1 outbox = new OutboxTable1();
    outbox.setAggregateType("CustomerV2");
    outbox.setAggregateId(savedCustomer.getId().toString());
    outbox.setPayload(writePayload(request));
    OutboxTable1 savedOutbox = outboxRepository.save(outbox);

    TransformationRecord record = new TransformationRecord();
    record.setOutboxId(savedOutbox.getId().toString());
    transformationRepository.save(record);

    return savedCustomer;
  }

  private String writePayload(V2CustomerRequest request) {
    try {
      return objectMapper.writeValueAsString(request);
    } catch (Exception ex) {
      throw new IllegalStateException("Unable to serialize V2 payload", ex);
    }
  }
}
