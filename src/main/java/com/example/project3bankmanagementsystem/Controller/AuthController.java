package com.example.project3bankmanagementsystem.Controller;

import com.example.project3bankmanagementsystem.Api.ApiResponse;
import com.example.project3bankmanagementsystem.DTOIn.CustomerDTOIn;
import com.example.project3bankmanagementsystem.DTOIn.EmployeeDTOIn;
import com.example.project3bankmanagementsystem.Entity.User;
import com.example.project3bankmanagementsystem.Service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ADMIN
    @GetMapping("/get")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(200).body(authService.getAllUsers());
    }

    // PUBLIC
    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody @Valid CustomerDTOIn customerDTOIn) {
        authService.registerCustomer(customerDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Customer registered successfully"));
    }

    // ADMIN
    @PostMapping("/register-employee")
    public ResponseEntity<?> registerEmployee(@RequestBody @Valid EmployeeDTOIn employeeDTOIn) {
        authService.registerEmployee(employeeDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Employee registered successfully"));
    }

    // ADMIN
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal User user, @PathVariable Integer userId) {
        authService.deleteUser(user.getId(), userId);
        return ResponseEntity.status(200).body(new ApiResponse("User deleted successfully"));
    }
}