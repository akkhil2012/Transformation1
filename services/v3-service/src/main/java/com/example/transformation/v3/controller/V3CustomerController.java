package com.example.transformation.v3.controller;

import com.example.transformation.v3.dto.V3CustomerRequest;
import com.example.transformation.v3.entity.CustomerV3;
import com.example.transformation.v3.service.V3CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/customers")
public class V3CustomerController {
  private final V3CustomerService service;

  public V3CustomerController(V3CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerV3 createCustomer(@RequestBody V3CustomerRequest request) {
    return service.createCustomer(request);
  }
}
