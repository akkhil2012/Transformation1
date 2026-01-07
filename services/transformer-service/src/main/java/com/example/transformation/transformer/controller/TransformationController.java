package com.example.transformation.transformer.controller;

import com.example.transformation.transformer.dto.V2CustomerPayload;
import com.example.transformation.transformer.dto.V3CustomerPayload;
import com.example.transformation.transformer.service.TransformationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transform")
public class TransformationController {
  private final TransformationService service;

  public TransformationController(TransformationService service) {
    this.service = service;
  }

  @PostMapping("/v2-to-v3")
  public V3CustomerPayload transform(@RequestBody V2CustomerPayload payload) {
    return service.transform(payload);
  }
}
