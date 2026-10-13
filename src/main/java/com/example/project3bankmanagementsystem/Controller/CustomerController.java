package com.example.project3bankmanagementsystem.Controller;

import com.example.project3bankmanagementsystem.Api.ApiResponse;
import com.example.project3bankmanagementsystem.DTOIn.CustomerDTOIn;
import com.example.project3bankmanagementsystem.Entity.User;
import com.example.project3bankmanagementsystem.Service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    // EMPLOYEE, ADMIN
    @GetMapping("/get")
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.status(200).body(customerService.getAllCustomers());
    }

    // CUSTOMER
    @GetMapping("/get/my")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(customerService.getMyProfile(user.getId()));
    }

    // CUSTOMER
    @PutMapping("/update")
    public ResponseEntity<?> updateCustomer(@AuthenticationPrincipal User user, @RequestBody @Valid CustomerDTOIn customerDTOIn) {
        customerService.updateCustomer(user.getId(), customerDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Customer updated successfully"));
    }

    // CUSTOMER
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteCustomer(@AuthenticationPrincipal User user) {
        customerService.deleteCustomer(user.getId());
        return ResponseEntity.status(200).body(new ApiResponse("Customer deleted successfully"));
    }
}