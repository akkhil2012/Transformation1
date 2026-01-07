package com.example.transformation.v2.repository;

import com.example.transformation.v2.entity.TransformationRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransformationRecordRepository extends JpaRepository<TransformationRecord, Long> {
}
