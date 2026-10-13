package com.example.project3bankmanagementsystem.Controller;

import com.example.project3bankmanagementsystem.Api.ApiResponse;
import com.example.project3bankmanagementsystem.DTOIn.EmployeeDTOIn;
import com.example.project3bankmanagementsystem.Entity.User;
import com.example.project3bankmanagementsystem.Service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    // ADMIN
    @GetMapping("/get")
    public ResponseEntity<?> getAllEmployees() {
        return ResponseEntity.status(200).body(employeeService.getAllEmployees());
    }

    // EMPLOYEE
    @GetMapping("/get/my")
    public ResponseEntity<?> getMyProfile(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(employeeService.getMyProfile(user.getId()));
    }

    // ADMIN
    @PutMapping("/update/{employeeId}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer employeeId, @RequestBody @Valid EmployeeDTOIn employeeDTOIn) {
        employeeService.updateEmployee(employeeId, employeeDTOIn);
        return ResponseEntity.status(200).body(new ApiResponse("Employee updated successfully"));
    }

    // ADMIN
    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.status(200).body(new ApiResponse("Employee deleted successfully"));
    }
}