package com.example.project3bankmanagementsystem.DTOOut;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTOOut {

    private Integer id;
    private String username;
    private String name;
    private String email;
    private String position;
    private Double salary;
}
