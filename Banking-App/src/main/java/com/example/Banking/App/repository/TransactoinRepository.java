package com.example.Banking.App.repository;

import com.example.Banking.App.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactoinRepository extends JpaRepository<Transaction , Long> {
    List<Transaction> findByAccountId(Long accountId);
}
