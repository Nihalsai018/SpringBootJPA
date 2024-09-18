package com.example.springboot.JPA.service;

import com.example.springboot.JPA.Exception.ResourceNotFoundException;
import com.example.springboot.JPA.Repository.EmployeRepositry;
import com.example.springboot.JPA.model.Customer;
import com.example.springboot.JPA.model.Employee;
import com.example.springboot.JPA.service.ServiceInterface.EmployeInterface;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.config.RequestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.client.RestClientSsl;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.time.Duration;
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

    public SimpleClientHttpRequestFactory getClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();

        clientHttpRequestFactory.setConnectTimeout(Duration.ofMinutes(1));
        clientHttpRequestFactory.setReadTimeout(Duration.ofMillis(4000));
        return clientHttpRequestFactory;
    }

    @Autowired
    public EmployeService(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .requestFactory(getClientHttpRequestFactory())
               .baseUrl("http://localhost:8082")
                .build();
    }

    @Override
    public Customer saveEmploye(Employee employee) {
        Customer customer = new Customer();
        customer.setCustomerFirstName(employee.getFirstName());
        customer.setCustomerLastName(employee.getLastName());
        customer.setCustomerEmail(employee.getEmail());


            ResponseEntity<Customer> response =  restClient.post()
                    .uri("/customerController/saverecords")
                   // .uri("https://outlook.office.com/mail/")
                    .body(customer)
                    .retrieve()
                    .toEntity(Customer.class);

        return response.getBody();
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
                    if (emp.getFirstName() != null && emp.getLastName() != null) {
                        externalData.add(emp);
                    }
                }
            }
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        List<Employee> combinedData = new ArrayList<>(localData);
        combinedData.addAll(externalData);

        combinedData = combinedData.stream()
                .filter(distinctByKey(Employee::getId))
                .collect(Collectors.toList());

        return combinedData;
    }

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
                    .uri("/customerController/{id}"+ id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        employeRepositry.deleteById(id);
    }
}
