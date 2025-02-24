package com.example.Banking.App.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor



public class LoanDto {
    private Long id;
    private Long accountId;
    private double amount;
    private String action; // CREATED, PAYMENT, PAID_OFF
    private LocalDate timestamp;


}
