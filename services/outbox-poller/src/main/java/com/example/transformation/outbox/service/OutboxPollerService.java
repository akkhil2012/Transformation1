package com.example.transformation.outbox.service;

import com.example.transformation.outbox.client.V2CustomerPayload;
import com.example.transformation.outbox.client.V3CustomerPayload;
import com.example.transformation.outbox.entity.OutboxTable1;
import com.example.transformation.outbox.entity.TransformationRecord;
import com.example.transformation.outbox.entity.TransformationRecord.Status;
import com.example.transformation.outbox.repository.OutboxTable1Repository;
import com.example.transformation.outbox.repository.TransformationRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class OutboxPollerService {
  private static final Logger LOGGER = LoggerFactory.getLogger(OutboxPollerService.class);

  private final TransformationRecordRepository transformationRepository;
  private final OutboxTable1Repository outboxRepository;
  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper;

  public OutboxPollerService(
      TransformationRecordRepository transformationRepository,
      OutboxTable1Repository outboxRepository,
      ObjectMapper objectMapper
  ) {
    this.transformationRepository = transformationRepository;
    this.outboxRepository = outboxRepository;
    this.objectMapper = objectMapper;
  }

  @Scheduled(fixedDelayString = "${poller.delay-ms:5000}")
  @Transactional
  public void pollOutbox() {
    List<TransformationRecord> pending = transformationRepository.findByStatus(Status.PENDING);
    for (TransformationRecord record : pending) {
      OutboxTable1 outbox = outboxRepository.findById(Long.parseLong(record.getOutboxId()))
          .orElse(null);
      if (outbox == null) {
        LOGGER.warn("Outbox record not found for id {}", record.getOutboxId());
        continue;
      }

      V2CustomerPayload v2Payload = readPayload(outbox.getPayload());
      V3CustomerPayload v3Payload = callTransformer(v2Payload);
      callV3Service(v3Payload);

      record.setStatus(Status.COMPLETED);
      transformationRepository.save(record);
    }
  }

  private V2CustomerPayload readPayload(String json) {
    try {
      return objectMapper.readValue(json, V2CustomerPayload.class);
    } catch (Exception ex) {
      throw new IllegalStateException("Unable to parse outbox payload", ex);
    }
  }

  private V3CustomerPayload callTransformer(V2CustomerPayload payload) {
    ResponseEntity<V3CustomerPayload> response = restTemplate.exchange(
        "http://localhost:8083/api/transform/v2-to-v3",
        HttpMethod.POST,
        new HttpEntity<>(payload),
        V3CustomerPayload.class
    );
    return response.getBody();
  }

  private void callV3Service(V3CustomerPayload payload) {
    restTemplate.exchange(
        "http://localhost:8082/api/v3/customers",
        HttpMethod.POST,
        new HttpEntity<>(payload),
        Void.class
    );
  }
}
