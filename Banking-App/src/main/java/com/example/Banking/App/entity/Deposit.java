package com.example.Banking.App.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import static java.lang.Math.pow;


@Entity
@Table(name = "deposit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double principal;
    private double interestEarned;
    private double interestRate;
    private int termInMonths;
    private LocalDate startDate;
    private LocalDate maturityDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "depositer_account_id")
    private Account depositerAccount;
    @Enumerated(EnumType.STRING) // Assuming Loan_Status is an enum
    @Column(name = "status")
    private DepositStatus status;


    public double earnedMoney(double amount , double interRate , int months){
        double totalMoney =  amount * pow((1+interRate) , months/12);
        double earnedMoney = totalMoney - amount;
        return earnedMoney;
    }

}
