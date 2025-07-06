package com.fiap.pagamento.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("PÓS GRADUAÇÃO - FIAP 2025 - SERVIÇO DE PAGAMENTO")
                        .version("1.0.0")
                        .description("Microsserviço responsável pelo processamento de pagamentos dos pedidos. Realiza a validação dos dados de pagamento, simula integrações com sistemas externos, controla o status das transações e garante a atualização correta do status dos pedidos conforme o resultado do pagamento."));
    }
}