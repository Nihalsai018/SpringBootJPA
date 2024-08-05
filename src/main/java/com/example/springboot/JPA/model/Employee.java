package com.example.springboot.JPA.model;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

@Data
@Entity
@Setter
@Getter
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @Column(name = "first_name", unique = true)
    private String firstName;

    @Column(name = "last-name")
    private String lastName;

    @Column(name = "email")
    private String email;



}
