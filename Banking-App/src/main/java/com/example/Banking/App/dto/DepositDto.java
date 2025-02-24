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
public class DepositDto {
    private Long id;
    private Long accountId;
    private double amount;
    private String action; // CREATED, WITHDRAWN, CLOSED
    private LocalDate timestamp;


}