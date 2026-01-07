package com.example.transformation.v3.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "customer_profiles", schema = "schemav3")
public class CustomerProfile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String addressLine1;
  private String city;

  @OneToOne
  @JoinColumn(name = "customer_id")
  private CustomerV3 customer;

  public Long getId() {
    return id;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public CustomerV3 getCustomer() {
    return customer;
  }

  public void setCustomer(CustomerV3 customer) {
    this.customer = customer;
  }
}
