package com.example.project3bankmanagementsystem.Repository;

import com.example.project3bankmanagementsystem.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {

    User findUserById(Integer id);

    User findUserByUsername(String username);

    User findUserByEmail(String email);
}