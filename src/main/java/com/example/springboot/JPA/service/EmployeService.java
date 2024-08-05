package com.example.springboot.JPA.service;

import com.example.springboot.JPA.Exception.ResourceNotFoundException;
import com.example.springboot.JPA.Repository.EmployeRepositry;
import com.example.springboot.JPA.model.Customer;
import com.example.springboot.JPA.model.Employee;
import com.example.springboot.JPA.service.ServiceInterface.EmployeInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class EmployeService implements EmployeInterface {

    @Autowired
    private EmployeRepositry employeRepositry;

    private final RestClient restClient;



    @Autowired
    public EmployeService(RestClient.Builder restClientBuilder, RestClientSsl ssl) {
        this.restClient = restClientBuilder.baseUrl("http://localhost:8082")
             //   .apply(ssl.fromBundle("client"))
                .build();
    }

    @Override
    public Employee saveEmploye(Employee employee) {
        Customer customer = new Customer();
        customer.setCustomerFirstName(employee.getFirstName());
        customer.setCustomerLastName(employee.getLastName());
        customer.setCustomerEmail(employee.getEmail());

        try {
            restClient.post()
                    .uri("/customerController/saverecords")
                    .body(customer)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return employeRepositry.save(employee);
    }

    @Override
    public List<Employee> getAllEmployee() {
        List<Employee> localData = employeRepositry.findAll();
        List<Employee> externalData = new ArrayList<>();

        try {
            Employee[] response = restClient.get()
                    .uri("/customerController/getrecords")
                    .retrieve()
                    .body(Employee[].class);

            if (response != null) {
                for (Employee emp : response) {
                    // Check for null values and skip if necessary
                    if (emp.getFirstName() != null && emp.getLastName() != null) {
                        externalData.add(emp);
                    }
                }
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        // Combine local data with external data, filtering out duplicates
        List<Employee> combinedData = new ArrayList<>(localData);
        combinedData.addAll(externalData);

        // Remove duplicates based on 'id'
        combinedData = combinedData.stream()
                .filter(distinctByKey(Employee::getId))
                .collect(Collectors.toList());

        return combinedData;
    }

    // Helper method to filter duplicates based on key
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }


    @Override
    public List<Employee> getEmployeesByIds(List<Long> ids) {
        return employeRepositry.findAllById(ids);
    }

    @Override
    public Employee getbyId(Long id) {
        Customer customer = null;
        try {
            customer = restClient.get()
                    .uri("/customerController/getrecordById/{id}", id)
                    .retrieve()
                    .body(Customer.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return employeRepositry.findById(id).orElse(null);
    }

    @Override
    public Employee updateRecords(Long id, Employee employee) throws ResourceNotFoundException {
        if (employeRepositry.existsById(id)) {
            employee.setId(id);
            try {
                restClient.put()
                        .uri("/customerController/{id}", id)
                        .body(employee)
                        .retrieve()
                        .toBodilessEntity();
            } catch (RestClientException e) {
                e.printStackTrace();
            }

            return employeRepositry.save(employee);
        } else {
            throw new ResourceNotFoundException("Employee", "id", id);
        }
    }

    @Override
    public void deleteRecords(Long id) {
        try {
            restClient.delete()
                    .uri("/customerController/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        employeRepositry.deleteById(id);
    }
}
