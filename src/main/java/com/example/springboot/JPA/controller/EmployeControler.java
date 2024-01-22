package com.example.springboot.JPA.controller;

import com.example.springboot.JPA.Exception.ResourceNotFoundException;
import com.example.springboot.JPA.Repository.EmployeRepositry;
import com.example.springboot.JPA.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.springboot.JPA.service.EmployeService;

import java.util.List;


@RestController
@RequestMapping(value = "/EmployeControler")
public class EmployeControler {

    @Autowired
    private EmployeService employeService;


    @PostMapping("/saverecords")

    public ResponseEntity<Employee> saveEmploye(@RequestBody Employee employee){
        return new ResponseEntity<Employee>(employeService.saveEmploye(employee), HttpStatus.CREATED);
    }


    @PostMapping(value = "/postEmploye")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee saveEmployee(@RequestBody Employee employee){
        return  employeService.saveEmploye(employee);

    }

    @GetMapping(value = "/getEmploye")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<Employee> getAllEmploye(){
        return  employeService.getAllEmployee();

    }


    @GetMapping("/getEmployeesByIds/{ids}")
    public List<Employee> getEmployeesByIds(@PathVariable List<Long> ids) {
        return employeService.getEmployeesByIds(ids);
    }





    @GetMapping("/getbyId/{getbyId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Employee getbyId(@PathVariable("getbyId") Long id){
        return employeService.getbyId(id);
    }

    @PutMapping(value = "/update/{updateid}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Employee update(@PathVariable("updateid") Long id,@RequestBody Employee employee) throws ResourceNotFoundException {

        return employeService.updateRecords(id,employee);
    }


    @DeleteMapping(value = "/deleteRecords/{deletebyid}")
    public void deleterecords(@PathVariable("deletebyid") Long id){
        employeService.deleteRecords(id);


    }







}

