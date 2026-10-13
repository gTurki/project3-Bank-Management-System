package com.example.project3bankmanagementsystem.Service;

import com.example.project3bankmanagementsystem.Api.ApiException;
import com.example.project3bankmanagementsystem.DTOIn.CustomerDTOIn;
import com.example.project3bankmanagementsystem.DTOIn.EmployeeDTOIn;
import com.example.project3bankmanagementsystem.DTOOut.UserDTOOut;
import com.example.project3bankmanagementsystem.Entity.Customer;
import com.example.project3bankmanagementsystem.Entity.Employee;
import com.example.project3bankmanagementsystem.Entity.User;
import com.example.project3bankmanagementsystem.Repository.AuthRepository;
import com.example.project3bankmanagementsystem.Repository.CustomerRepository;
import com.example.project3bankmanagementsystem.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;


    // for the admin to get all users
    public List<UserDTOOut> getAllUsers() {
        List<User> users = authRepository.findAll();
        List<UserDTOOut> userDTOOuts = new ArrayList<>();

        for (User u : users) {
            userDTOOuts.add(new UserDTOOut(u.getId(), u.getUsername(), u.getName(), u.getEmail(), u.getRole()));
        }
        return userDTOOuts;
    }

    // for anyone to register a customer
    public void registerCustomer(CustomerDTOIn customerDTOIn) {
        checkUsernameAndEmail(customerDTOIn.getUsername(), customerDTOIn.getEmail());

        User user = new User();
        user.setUsername(customerDTOIn.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(customerDTOIn.getPassword()));
        user.setName((customerDTOIn.getName()));
        user.setEmail(customerDTOIn.getEmail());
        user.setRole("CUSTOMER");
        authRepository.save(user);

        Customer customer = new Customer();
        customer.setPhoneNumber(customerDTOIn.getPhoneNumber());
        customer.setUser(user);
        customerRepository.save(customer);
    }

    public void registerEmployee(EmployeeDTOIn employeeDTOIn) {
        checkUsernameAndEmail(employeeDTOIn.getUsername(), employeeDTOIn.getEmail());

        User user = new User();
        user.setUsername(employeeDTOIn.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(employeeDTOIn.getPassword()));
        user.setName(employeeDTOIn.getName());
        user.setEmail(employeeDTOIn.getEmail());
        user.setRole("EMPLOYEE");
        authRepository.save(user);

        Employee employee = new Employee();
        employee.setPosition(employeeDTOIn.getPosition());
        employee.setSalary(employeeDTOIn.getSalary());
        employee.setUser(user);
        employeeRepository.save(employee);
    }

    // the admin will delete any user and the cascade deletes the customer or employee accounts
    public void deleteUser(Integer adminId, Integer userId) {
        User user = authRepository.findUserById(userId);

        if (user == null) {
            throw new ApiException("User not found");
        }
        if (user.getId().equals(adminId)) {
            throw new ApiException("You cannot delete yourself");
        }
        authRepository.delete(user);
    }

    public void checkUsernameAndEmail(String username, String email) {
        if (authRepository.findUserByUsername(username) != null) {
            throw new ApiException("username already exists");
        }

        if (authRepository.findUserByEmail(email) != null) {
            throw new ApiException("email already exists");
        }
    }


}
