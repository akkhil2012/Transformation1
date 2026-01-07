package com.example.transformation.v3.service;

import com.example.transformation.v3.dto.V3CustomerRequest;
import com.example.transformation.v3.dto.V3OrderRequest;
import com.example.transformation.v3.entity.CustomerProfile;
import com.example.transformation.v3.entity.CustomerV3;
import com.example.transformation.v3.entity.OrderV3;
import com.example.transformation.v3.repository.CustomerV3Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class V3CustomerService {
  private final CustomerV3Repository customerRepository;

  public V3CustomerService(CustomerV3Repository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Transactional
  public CustomerV3 createCustomer(V3CustomerRequest request) {
    CustomerV3 customer = new CustomerV3();
    customer.setExternalId(request.externalId());
    customer.setFullName(request.fullName());
    customer.setEmail(request.email());

    CustomerProfile profile = new CustomerProfile();
    profile.setAddressLine1(request.addressLine1());
    profile.setCity(request.city());
    customer.setProfile(profile);

    if (request.orders() != null) {
      for (V3OrderRequest orderRequest : request.orders()) {
        OrderV3 order = new OrderV3();
        order.setOrderNumber(orderRequest.orderNumber());
        order.setTotal(orderRequest.total());
        customer.addOrder(order);
      }
    }

    return customerRepository.save(customer);
  }
}
