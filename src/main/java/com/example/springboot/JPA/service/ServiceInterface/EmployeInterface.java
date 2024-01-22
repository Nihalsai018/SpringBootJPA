package com.example.springboot.JPA.service.ServiceInterface;

import com.example.springboot.JPA.Exception.ResourceNotFoundException;
import com.example.springboot.JPA.model.Employee;

import java.util.List;

public interface EmployeInterface {
 public Employee saveEmploye(Employee employee);
 public List<Employee> getAllEmployee();

 List<Employee> getEmployeesByIds(List<Long> ids);

 public  Employee getbyId(Long id);



 public  Employee updateRecords(Long id,Employee employee) throws ResourceNotFoundException;

 public void deleteRecords(Long id);

}
