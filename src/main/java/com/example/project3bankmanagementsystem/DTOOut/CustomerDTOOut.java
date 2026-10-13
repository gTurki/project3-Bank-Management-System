package com.example.project3bankmanagementsystem.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTOOut {

    private Integer id;
    private String username;
    private String name;
    private String email;
    private String phoneNumber;
    private List<AccountDTOOut> accounts;
}