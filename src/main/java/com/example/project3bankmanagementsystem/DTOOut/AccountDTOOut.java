package com.example.project3bankmanagementsystem.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTOOut {

    private Integer id;
    private String accountNumber;
    private Double balance;
    private Boolean isActive;
}