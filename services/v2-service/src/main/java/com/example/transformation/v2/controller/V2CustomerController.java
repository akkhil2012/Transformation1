package com.example.transformation.v2.controller;

import com.example.transformation.v2.dto.V2CustomerRequest;
import com.example.transformation.v2.entity.CustomerV2;
import com.example.transformation.v2.service.V2CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/customers")
public class V2CustomerController {
  private final V2CustomerService service;

  public V2CustomerController(V2CustomerService service) {
    this.service = service;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public CustomerV2 createCustomer(@RequestBody V2CustomerRequest request) {
    return service.createCustomer(request);
  }
}
