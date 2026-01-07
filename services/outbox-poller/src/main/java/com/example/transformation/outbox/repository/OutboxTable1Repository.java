package com.example.transformation.outbox.repository;

import com.example.transformation.outbox.entity.OutboxTable1;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxTable1Repository extends JpaRepository<OutboxTable1, Long> {
}
