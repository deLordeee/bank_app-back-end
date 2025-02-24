package com.example.Banking.App.entity.kafka;

import com.example.Banking.App.dto.AccountDto;
import com.example.Banking.App.dto.TransactionDto;
import com.example.Banking.App.dto.DepositDto;
import com.example.Banking.App.dto.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public Producer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(AccountDto accountDto) {
        LOGGER.info(String.format("Account created -> %s", accountDto.toString()));
        kafkaTemplate.send("account-topic", accountDto);
    }
    public void sendTransactionMessage(TransactionDto transactionDto) {
        LOGGER.info("Attempting to send transaction: {}", transactionDto);
        try {
            kafkaTemplate.send("transaction-topic", transactionDto);
            LOGGER.info("Successfully sent transaction to Kafka");
        } catch (Exception e) {
            LOGGER.error("Failed to send transaction to Kafka: ", e);
        }
    }
    public void sendMessage(DepositDto depositDto) {
        LOGGER.info("Sending deposit event: {}", depositDto);
        try {
            kafkaTemplate.send("deposit-topic", depositDto);
            LOGGER.info("Successfully sent deposit event");
        } catch (Exception e) {
            LOGGER.error("Failed to send deposit event: ", e);
        }
    }

    public void sendMessage(LoanDto loanDto) {
        LOGGER.info("Sending loan event: {}", loanDto);
        try {
            kafkaTemplate.send("loan-topic", loanDto);
            LOGGER.info("Successfully sent loan event");
        } catch (Exception e) {
            LOGGER.error("Failed to send loan event: ", e);
        }
    }
}