package com.fiap.pagamento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.fiap.pagamento.gateway")
public class PagamentoServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PagamentoServiceApplication.class, args);
	}
}