package com.example.project3bankmanagementsystem.Service;

import com.example.project3bankmanagementsystem.Api.ApiException;
import com.example.project3bankmanagementsystem.DTOIn.CustomerDTOIn;
import com.example.project3bankmanagementsystem.DTOOut.AccountDTOOut;
import com.example.project3bankmanagementsystem.DTOOut.CustomerDTOOut;
import com.example.project3bankmanagementsystem.Entity.Account;
import com.example.project3bankmanagementsystem.Entity.Customer;
import com.example.project3bankmanagementsystem.Entity.User;
import com.example.project3bankmanagementsystem.Repository.AccountRepository;
import com.example.project3bankmanagementsystem.Repository.AuthRepository;
import com.example.project3bankmanagementsystem.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final AuthRepository authRepository;
    private final AccountRepository accountRepository;

    // this can be used by the admin and the employee to get all customers
    public List<CustomerDTOOut> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        List<CustomerDTOOut> customerDTOOuts = new ArrayList<>();
        for (Customer customer : customers) {
            customerDTOOuts.add(convertToDTO(customer));
        }
        return customerDTOOuts;
    }

    // for the customer to view their profile
    public CustomerDTOOut getMyProfile(Integer userId) {
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        return convertToDTO(customer);
    }

    // for the customer to update their profile
    public void updateCustomer(Integer userId, CustomerDTOIn customerDTOIn) {
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }

        User usernameOwner = authRepository.findUserByUsername(customerDTOIn.getUsername());
        if (usernameOwner != null && !usernameOwner.getId().equals(userId)) {
            throw new ApiException("Username already exists");
        }
        User emailOwner = authRepository.findUserByEmail(customerDTOIn.getEmail());
        if (emailOwner != null && !emailOwner.getId().equals(userId)) {
            throw new ApiException("Email already exists");
        }

        User user = customer.getUser();
        user.setUsername(customerDTOIn.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(customerDTOIn.getPassword()));
        user.setName(customerDTOIn.getName());
        user.setEmail(customerDTOIn.getEmail());
        authRepository.save(user);

        customer.setPhoneNumber(customerDTOIn.getPhoneNumber());
        customerRepository.save(customer);
    }

    // for the customer to delete their profile and the cascade deletes customer + accounts
    public void deleteCustomer(Integer userId) {
        User user = authRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found");
        }
        authRepository.delete(user);
    }

    // this converts a customer entity to a customerDTOOut
    private CustomerDTOOut convertToDTO(Customer customer) {
        List<AccountDTOOut> accountDTOOuts = new ArrayList<>();
        for (Account account : accountRepository.findAllByCustomer(customer)) {
            accountDTOOuts.add(new AccountDTOOut(account.getId(), account.getAccountNumber(), account.getBalance(), account.getIsActive()));
        }
        User user = customer.getUser();
        return new CustomerDTOOut(customer.getId(), user.getUsername(), user.getName(), user.getEmail(), customer.getPhoneNumber(), accountDTOOuts);
    }
}