package com.example.project3bankmanagementsystem.Service;

import com.example.project3bankmanagementsystem.Api.ApiException;
import com.example.project3bankmanagementsystem.DTOIn.EmployeeDTOIn;
import com.example.project3bankmanagementsystem.DTOOut.EmployeeDTOOut;
import com.example.project3bankmanagementsystem.Entity.Employee;
import com.example.project3bankmanagementsystem.Entity.User;
import com.example.project3bankmanagementsystem.Repository.AuthRepository;
import com.example.project3bankmanagementsystem.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final AuthRepository authRepository;

    // used by the admin to get all employees
    public List<EmployeeDTOOut> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        List<EmployeeDTOOut> employeeDTOOuts = new ArrayList<>();
        for (Employee employee : employees) {
            employeeDTOOuts.add(convertToDTO(employee));
        }
        return employeeDTOOuts;
    }

    // used by the employee to view their profile
    public EmployeeDTOOut getMyProfile(Integer userId) {
        Employee employee = employeeRepository.findEmployeeById(userId);
        if (employee == null) {
            throw new ApiException("Employee not found");
        }
        return convertToDTO(employee);
    }

    // used by the admin to update an employee, the position and salary are decided by the admin
    public void updateEmployee(Integer employeeId, EmployeeDTOIn employeeDTOIn) {
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null) {
            throw new ApiException("Employee not found");
        }

        User usernameOwner = authRepository.findUserByUsername(employeeDTOIn.getUsername());
        if (usernameOwner != null && !usernameOwner.getId().equals(employeeId)) {
            throw new ApiException("Username already exists");
        }
        User emailOwner = authRepository.findUserByEmail(employeeDTOIn.getEmail());
        if (emailOwner != null && !emailOwner.getId().equals(employeeId)) {
            throw new ApiException("Email already exists");
        }

        User user = employee.getUser();
        user.setUsername(employeeDTOIn.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(employeeDTOIn.getPassword()));
        user.setName(employeeDTOIn.getName());
        user.setEmail(employeeDTOIn.getEmail());
        authRepository.save(user);

        employee.setPosition(employeeDTOIn.getPosition());
        employee.setSalary(employeeDTOIn.getSalary());
        employeeRepository.save(employee);
    }

    // used by the admin to delete an employee, the cascade deletes the employee row
    public void deleteEmployee(Integer employeeId) {
        Employee employee = employeeRepository.findEmployeeById(employeeId);
        if (employee == null) {
            throw new ApiException("Employee not found");
        }
        authRepository.delete(employee.getUser());
    }

    // this converts an employee entity to an employeeDTOOut
    private EmployeeDTOOut convertToDTO(Employee employee) {
        User user = employee.getUser();
        return new EmployeeDTOOut(employee.getId(), user.getUsername(), user.getName(), user.getEmail(), employee.getPosition(), employee.getSalary());
    }
}