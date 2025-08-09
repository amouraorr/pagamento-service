package com.fiap.pagamento;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceApplicationUnitTest {

    @Test
    void deveVerificarSeClassePossuiAnotacaoSpringBootApplication() {
        boolean possuiAnotacao = PagamentoServiceApplication.class
                .isAnnotationPresent(SpringBootApplication.class);

        assertTrue(possuiAnotacao, "A classe deve possuir a anotação @SpringBootApplication");
    }

    @Test
    void deveExecutarMetodoMainComSucesso() {
        ConfigurableApplicationContext mockContext = org.mockito.Mockito.mock(ConfigurableApplicationContext.class);
        String[] args = {"--spring.profiles.active=test"};

        try (MockedStatic<SpringApplication> springAppMock = mockStatic(SpringApplication.class)) {
            springAppMock.when(() -> SpringApplication.run(eq(PagamentoServiceApplication.class), any(String[].class)))
                    .thenReturn(mockContext);

            PagamentoServiceApplication.main(args);

            springAppMock.verify(() -> SpringApplication.run(eq(PagamentoServiceApplication.class), any(String[].class)));
        }
    }
}