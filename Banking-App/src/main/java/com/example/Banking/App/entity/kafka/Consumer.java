package com.example.Banking.App.entity.kafka;

import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.dto.TransactionDto;
import com.example.Banking.App.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    @KafkaListener(topics = "account-topic", groupId = "banking-group")
    public void consumeAccount(AccountDto accountDto) {
        LOGGER.info("Account event received -> {}", accountDto);
    }

    @KafkaListener(topics = "transaction-topic", groupId = "banking-group")
    public void consumeTransaction(TransactionDto transactionDto) {
        LOGGER.info("Transaction event received -> {}", transactionDto);
    }
}