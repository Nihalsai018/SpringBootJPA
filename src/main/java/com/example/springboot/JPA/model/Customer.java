package com.example.springboot.JPA.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity

// if i keep entity it is belong to database opertion the reason of keeping here to in customer class iam keeping as reporsitry and using customer clss

public class Customer {
    @Id  //act as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // it helps to auto increment the id in database we gave in service method
    @JsonProperty("id")   // to customize and it representd the class level varibles , to hide the class levevel properties
    private  int customerId;
    @JsonProperty("firstname")
    private String customerFirstName;
    @JsonProperty("lastname")
    private String customerLastName;
    @JsonProperty("email")
    private String customerEmail;

    public Customer(int customerId, String customerFirstName, String customerLastName, String customerEmail) {
    }

    public Customer() {
    }


    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }


}
