package com.example.project3bankmanagementsystem.Service;

import com.example.project3bankmanagementsystem.Api.ApiException;
import com.example.project3bankmanagementsystem.DTOOut.AccountDTOOut;
import com.example.project3bankmanagementsystem.Entity.Account;
import com.example.project3bankmanagementsystem.Entity.Customer;
import com.example.project3bankmanagementsystem.Repository.AccountRepository;
import com.example.project3bankmanagementsystem.Repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    // used by the employee and admin to view all customer accounts
    public List<AccountDTOOut> getAllAccounts() {
        return convertToDTOList(accountRepository.findAll());
    }

    // used by the customer to list their accounts
    public List<AccountDTOOut> getMyAccounts(Integer userId) {
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        return convertToDTOList(accountRepository.findAllByCustomer(customer));
    }

    // used by the customer to view their account details
    public AccountDTOOut getAccountDetails(Integer userId, Integer accountId) {
        Account account = getOwnedAccount(userId, accountId);
        return convertToDTO(account);
    }

    // used by the customer to create new account and only an employee can activate it
    public void createAccount(Integer userId, Account account) {
        Customer customer = customerRepository.findCustomerById(userId);
        if (customer == null) {
            throw new ApiException("Customer not found");
        }
        if (accountRepository.findAccountByAccountNumber(account.getAccountNumber()) != null) {
            throw new ApiException("Account number already exists");
        }
        account.setIsActive(false);
        account.setCustomer(customer);
        accountRepository.save(account);
    }

    // used by the employee and admin to activate an account
    public void activateAccount(Integer accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (account.getIsActive()) {
            throw new ApiException("Account is already active");
        }
        account.setIsActive(true);
        accountRepository.save(account);
    }

    // used by the employee and admin to block an account
    public void blockAccount(Integer accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (!account.getIsActive()) {
            throw new ApiException("Account is already blocked");
        }
        account.setIsActive(false);
        accountRepository.save(account);
    }

    // used by the customer to deposit money
    public void deposit(Integer userId, Integer accountId, Double amount) {
        checkAmount(amount);
        Account account = getOwnedAccount(userId, accountId);
        checkActive(account);

        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);
    }

    // used by the customer to withdraw money
    public void withdraw(Integer userId, Integer accountId, Double amount) {
        checkAmount(amount);
        Account account = getOwnedAccount(userId, accountId);
        checkActive(account);
        if (account.getBalance() < amount) {
            throw new ApiException("Insufficient balance");
        }

        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }

    // used by the customer to transfer money from their account to another account by account number
    @Transactional
    public void transfer(Integer userId, Integer fromAccountId, String toAccountNumber, Double amount) {
        checkAmount(amount);
        Account fromAccount = getOwnedAccount(userId, fromAccountId);
        checkActive(fromAccount);

        Account toAccount = accountRepository.findAccountByAccountNumber(toAccountNumber);
        if (toAccount == null) {
            throw new ApiException("Target account not found");
        }
        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new ApiException("You cannot transfer to the same account");
        }
        if (!toAccount.getIsActive()) {
            throw new ApiException("Target account is not active");
        }
        if (fromAccount.getBalance() < amount) {
            throw new ApiException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance() - amount);
        toAccount.setBalance(toAccount.getBalance() + amount);
        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }

    // used by the customer to view their account details
    private Account getOwnedAccount(Integer userId, Integer accountId) {
        Account account = accountRepository.findAccountById(accountId);
        if (account == null) {
            throw new ApiException("Account not found");
        }
        if (!account.getCustomer().getId().equals(userId)) {
            throw new ApiException("You are not allowed to access this account");
        }
        return account;
    }

    private void checkAmount(Double amount) {
        if (amount == null || amount <= 0) {
            throw new ApiException("Amount must be greater than 0");
        }
    }

    private void checkActive(Account account) {
        if (!account.getIsActive()) {
            throw new ApiException("Account is not active");
        }
    }

    private AccountDTOOut convertToDTO(Account account) {
        return new AccountDTOOut(account.getId(), account.getAccountNumber(), account.getBalance(), account.getIsActive());
    }

    private List<AccountDTOOut> convertToDTOList(List<Account> accounts) {
        List<AccountDTOOut> accountDTOOuts = new ArrayList<>();
        for (Account account : accounts) {
            accountDTOOuts.add(convertToDTO(account));
        }
        return accountDTOOuts;
    }
}