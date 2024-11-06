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
@Table(name = "loan")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double principal;
    private double remainToPay;
    private double interestRate;
    private int durationInYears;
    private double monthlyPayment;
    private LocalDate startDate;
    private LocalDate endDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "borrower_account_id")
    private Account borrowerAccount;
    @Enumerated(EnumType.STRING) // Assuming Loan_Status is an enum
    private Loan_Status status;


    public double calcMonthlyPayment(double principall , double interestRatee , int durationInYearss){
        double monthly_interest_rate = interestRatee/12;
        int total_numbers_payments = durationInYearss * 12;
        double numerator = principall * monthly_interest_rate* pow((1+monthly_interest_rate),total_numbers_payments);
        double denominator = pow((1+monthly_interest_rate),total_numbers_payments) - 1;
        double monthlyPayment =  numerator/denominator;
        return monthlyPayment;

    }


}
