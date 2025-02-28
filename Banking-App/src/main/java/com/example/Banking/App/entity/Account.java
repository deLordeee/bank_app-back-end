package com.example.Banking.App.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.management.relation.Role;
import java.util.Set;

@Entity
@Table(name = "accout")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @Column(name = "account_holder_name", unique = true, nullable = false)
    private String accountHolderName;
    private double balance;
    @Column(name = "account_password")
    private String password;
    @Column(name = "monthly_spent", nullable = false)
    private Double monthly_spending = 0.0;
    @Column(name = "monthly_earn", nullable = false)
    private Double monthly_earn = 0.0;
}
