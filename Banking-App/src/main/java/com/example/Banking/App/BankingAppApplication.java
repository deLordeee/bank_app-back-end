package com.example.Banking.App;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.EnableKafka;

@EnableKafka
@SpringBootApplication
class BankingAppApplication {

	public static void main(String[] args) {


		SpringApplication.run(BankingAppApplication.class, args);
	}

}
