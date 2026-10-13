package com.example.project3bankmanagementsystem.Repository;

import com.example.project3bankmanagementsystem.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    Customer findCustomerById(Integer id);
}