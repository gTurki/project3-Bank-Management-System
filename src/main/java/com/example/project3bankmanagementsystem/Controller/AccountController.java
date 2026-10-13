package com.example.project3bankmanagementsystem.Controller;

import com.example.project3bankmanagementsystem.Api.ApiResponse;
import com.example.project3bankmanagementsystem.Entity.Account;
import com.example.project3bankmanagementsystem.Entity.User;
import com.example.project3bankmanagementsystem.Service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    // EMPLOYEE, ADMIN
    @GetMapping("/get")
    public ResponseEntity<?> getAllAccounts() {
        return ResponseEntity.status(200).body(accountService.getAllAccounts());
    }

    // CUSTOMER
    @GetMapping("/get/my")
    public ResponseEntity<?> getMyAccounts(@AuthenticationPrincipal User user) {
        return ResponseEntity.status(200).body(accountService.getMyAccounts(user.getId()));
    }

    // CUSTOMER
    @GetMapping("/get/{accountId}")
    public ResponseEntity<?> getAccountDetails(@AuthenticationPrincipal User user, @PathVariable Integer accountId) {
        return ResponseEntity.status(200).body(accountService.getAccountDetails(user.getId(), accountId));
    }

    // CUSTOMER
    @PostMapping("/add")
    public ResponseEntity<?> createAccount(@AuthenticationPrincipal User user, @RequestBody @Valid Account account) {
        accountService.createAccount(user.getId(), account);
        return ResponseEntity.status(200).body(new ApiResponse("Account created successfully, waiting for activation"));
    }

    // EMPLOYEE, ADMIN
    @PutMapping("/activate/{accountId}")
    public ResponseEntity<?> activateAccount(@PathVariable Integer accountId) {
        accountService.activateAccount(accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account activated successfully"));
    }

    // EMPLOYEE, ADMIN
    @PutMapping("/block/{accountId}")
    public ResponseEntity<?> blockAccount(@PathVariable Integer accountId) {
        accountService.blockAccount(accountId);
        return ResponseEntity.status(200).body(new ApiResponse("Account blocked successfully"));
    }

    // CUSTOMER
    @PutMapping("/deposit/{accountId}/{amount}")
    public ResponseEntity<?> deposit(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @PathVariable Double amount) {
        accountService.deposit(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Deposit done successfully"));
    }

    // CUSTOMER
    @PutMapping("/withdraw/{accountId}/{amount}")
    public ResponseEntity<?> withdraw(@AuthenticationPrincipal User user, @PathVariable Integer accountId, @PathVariable Double amount) {
        accountService.withdraw(user.getId(), accountId, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Withdraw done successfully"));
    }

    // CUSTOMER
    @PutMapping("/transfer/{fromAccountId}/{toAccountNumber}/{amount}")
    public ResponseEntity<?> transfer(@AuthenticationPrincipal User user, @PathVariable Integer fromAccountId,
                                      @PathVariable String toAccountNumber, @PathVariable Double amount) {
        accountService.transfer(user.getId(), fromAccountId, toAccountNumber, amount);
        return ResponseEntity.status(200).body(new ApiResponse("Transfer done successfully"));
    }
}