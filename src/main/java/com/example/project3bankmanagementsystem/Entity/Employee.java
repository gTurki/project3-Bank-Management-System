package com.example.project3bankmanagementsystem.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Employee {

    @Id
    private Integer id;

    @NotEmpty(message = "position cannot be empty")
    @Column(columnDefinition = "varchar(30) not null")
    private String position;

    @NotNull(message = "Salary cannot be null")
    @PositiveOrZero(message = "Salary must be a non-negative number")
    @Column(columnDefinition = "double not null")
    private Double salary;

    @OneToOne
    @JsonIgnore
    @MapsId
    private User user;
}
