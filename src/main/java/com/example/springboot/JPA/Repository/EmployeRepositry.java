package com.example.springboot.JPA.Repository;

import com.example.springboot.JPA.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

//@Repository it is not mandtory because internally jpa has repositry annaotion

public interface EmployeRepositry extends JpaRepository<Employee,Long> {


}
