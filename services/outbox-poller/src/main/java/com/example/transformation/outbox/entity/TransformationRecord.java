package com.example.transformation.outbox.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "transformation_table", schema = "schemav2")
public class TransformationRecord {
  public enum Status {
    PENDING,
    COMPLETED
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String outboxId;

  @Enumerated(EnumType.STRING)
  private Status status = Status.PENDING;

  public Long getId() {
    return id;
  }

  public String getOutboxId() {
    return outboxId;
  }

  public void setOutboxId(String outboxId) {
    this.outboxId = outboxId;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }
}
