package com.example.springboot.JPA.service;

import com.example.springboot.JPA.Exception.ResourceNotFoundException;
import com.example.springboot.JPA.Repository.EmployeRepositry;
import com.example.springboot.JPA.model.Customer;
import com.example.springboot.JPA.model.Employee;
import com.example.springboot.JPA.service.ServiceInterface.EmployeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeService implements EmployeInterface {
    @Autowired
    private EmployeRepositry employeRepositry;

    @Value("${customerSaveUrl}")
    String SaveUrl;
    @Value("${customerPosturl}")
    String PostUrl;
    RestTemplate restTemplate=new RestTemplate();
    Customer customer=new Customer();
    HttpEntity<Customer> customerHttpEntity=new HttpEntity<>(customer,null);

    @Override
    public Employee saveEmploye(Employee employee) {
        customer.setCustomerFirstName(employee.getFirstName());
        customer.setCustomerLastName(employee.getLastName());
        customer.setCustomerEmail(employee.getEmail());
        restTemplate.exchange(SaveUrl, HttpMethod.POST,customerHttpEntity, Customer.class);
        return employeRepositry.save(employee);
    }
    @Override
    public List<Employee> getAllEmployee() {
        // Fetch data from the external database using RestTemplate
        ResponseEntity<List> externalDataResponse = restTemplate.exchange(PostUrl, HttpMethod.GET, customerHttpEntity, List.class);
        List<Employee> externalData = externalDataResponse.getBody();

        // Fetch data from the local database
        List<Employee> localData = employeRepositry.findAll();

        // Combine the data from both databases
        List<Employee> combinedData = new ArrayList<>();
        combinedData.addAll(localData);
        combinedData.addAll(externalData);

        return combinedData;
    }
//    @Override
//    public List<Employee> getAllEmployee() {
//        restTemplate.exchange(PostUrl, HttpMethod.GET, customerHttpEntity, List.class);
//        List<Employee> getall=employeRepositry.findAll();
//        return getall;
//    }

    @Override
    public List<Employee> getEmployeesByIds(List<Long> ids) {
        return employeRepositry.findAllById(ids);
    }
    @Override
    public Employee getbyId(Long id) {
         restTemplate.exchange("http://localhost:8082/customerController/getrecordById/{id}", HttpMethod.GET, null,  // No request entity for a GET request
                Customer.class,
                id
        );
         return employeRepositry.findById(id).orElse(null);
      //  return employeRepositry.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employe","id",id));
        //i want tp work on onlu returing employe data and i want to store the data an dretive data

        // exchange or we can do by getby object
        //Customer customer = restTemplate.getForObject(
        //                "http://localhost:8082/customerController/getrecordById/{id}",
        //                Customer.class,
        //                id
        //        );

    }

    @Override
    public Employee updateRecords(Long id, Employee employee) throws ResourceNotFoundException {
        if (employeRepositry.existsById(id)) {
            employee.setId(id);
            restTemplate.exchange("http://localhost:8082/customerController/{id}",HttpMethod.PUT,customerHttpEntity,Customer.class,id);
            return employeRepositry.save(employee);
        }

        else {
            throw new ResourceNotFoundException("Employee", "id", id);
        }

    }
    @Override
    public void deleteRecords(Long id) {
        restTemplate.delete("http://localhost:8082/customerController/{id}", id);
        employeRepositry.deleteById(id);

        // small dout :- here i have set of records presrent in both tables in customer, manually i added
        // some of records addditionally now if i want to delete from the employe how it is possible
        //ans:-if presrent it will delete if not there nothing happen
    }

}



