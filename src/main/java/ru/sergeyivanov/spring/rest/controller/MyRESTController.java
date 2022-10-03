package ru.sergeyivanov.spring.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.sergeyivanov.spring.rest.entity.Employee;
import ru.sergeyivanov.spring.rest.exception_handling.EmployeeIncorrectData;
import ru.sergeyivanov.spring.rest.exception_handling.NoSuchEmployeeException;
import ru.sergeyivanov.spring.rest.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class MyRESTController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping( "/employees")
    public List<Employee> showAllEmployees(){
        List<Employee> allEmployees = employeeService.getAllEmployees();
        return allEmployees;
    }

    @GetMapping("/employees/{id}") // this {id} passing to the parameter of method
    public Employee getEmployee(@PathVariable int id){ // @PathVariable is using for getting value of request from address
        Employee employee = employeeService.getEmployee(id);

        if (employee == null){
            throw new NoSuchEmployeeException("There is no employee with ID = " + id + " in the DataBase");
        }

        return employee; //passing is not object employee and JSON, because Spring and Jackson
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(NoSuchEmployeeException exception){
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<EmployeeIncorrectData> handleException(Exception exception){
        EmployeeIncorrectData data = new EmployeeIncorrectData();
        data.setInfo(exception.getMessage());

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/employees")
    public Employee addNewEmployee(@RequestBody Employee employee){ //we are passing JSON? but Spring convert parameter to class
        employeeService.saveEmployee(employee);
        return employee;
    }

    @PutMapping("/employees")
    public Employee updateEmployee(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        return employee;
    }

    @DeleteMapping("/employees/{id}")
    public String deleteEmployee(@PathVariable int id){
        Employee employee = employeeService.getEmployee(id);
        if (employee == null){
            throw new NoSuchEmployeeException("There is no employee with ID = " + id + " in the DataBase");
        }


        employeeService.deleteEmployee(id);
        return "employee with id = " + id + " was deleted";
    }
}
