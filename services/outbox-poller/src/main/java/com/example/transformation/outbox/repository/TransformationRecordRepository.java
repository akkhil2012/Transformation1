package com.example.transformation.outbox.repository;

import com.example.transformation.outbox.entity.TransformationRecord;
import com.example.transformation.outbox.entity.TransformationRecord.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransformationRecordRepository extends JpaRepository<TransformationRecord, Long> {
  List<TransformationRecord> findByStatus(Status status);
}
