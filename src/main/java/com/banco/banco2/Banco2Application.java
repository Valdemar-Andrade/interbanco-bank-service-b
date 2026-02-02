package com.banco.banco2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class Banco2Application {

	public static void main(String[] args) {
		SpringApplication.run(Banco2Application.class, args);
	}

}
