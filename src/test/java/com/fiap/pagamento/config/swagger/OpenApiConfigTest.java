package com.fiap.pagamento.config.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    private OpenApiConfig openApiConfig;

    @BeforeEach
    void setUp() {
        openApiConfig = new OpenApiConfig();
    }

    @Test
    void deveVerificarSeClassePossuiAnotacaoConfiguration() {
        // Given / When
        boolean possuiAnotacao = OpenApiConfig.class.isAnnotationPresent(Configuration.class);

        // Then
        assertTrue(possuiAnotacao);
    }

    @Test
    void deveVerificarSeMetodoCustomOpenAPIPossuiAnotacaoBean() throws NoSuchMethodException {
        // Given
        Method metodo = OpenApiConfig.class.getMethod("customOpenAPI");

        // When
        boolean possuiAnotacao = metodo.isAnnotationPresent(Bean.class);

        // Then
        assertTrue(possuiAnotacao);
    }

    @Test
    void deveRetornarObjetoOpenAPINaoNulo() {
        // When
        OpenAPI resultado = openApiConfig.customOpenAPI();

        // Then
        assertNotNull(resultado);
    }

    @Test
    void deveConfigurarOpenAPIComInformacoesCorretas() {
        // When
        OpenAPI resultado = openApiConfig.customOpenAPI();

        // Then
        assertNotNull(resultado.getInfo());

        Info info = resultado.getInfo();
        assertEquals("PÓS GRADUAÇÃO - FIAP 2025 - SERVIÇO DE PAGAMENTO", info.getTitle());
        assertEquals("1.0.0", info.getVersion());
        assertEquals("Microsserviço responsável pelo processamento de pagamentos dos pedidos. Realiza a validação dos dados de pagamento, simula integrações com sistemas externos, controla o status das transações e garante a atualização correta do status dos pedidos conforme o resultado do pagamento.", info.getDescription());
    }

    @Test
    void deveRetornarInstanciaOpenAPIValidaComInfo() {
        // When
        OpenAPI resultado = openApiConfig.customOpenAPI();

        // Then
        assertNotNull(resultado);
        assertNotNull(resultado.getInfo());
        assertTrue(resultado.getInfo().getTitle().contains("FIAP"));
        assertTrue(resultado.getInfo().getTitle().contains("PAGAMENTO"));
    }

    @Test
    void deveVerificarTipoDeRetornoDoMetodo() throws NoSuchMethodException {
        // Given
        Method metodo = OpenApiConfig.class.getMethod("customOpenAPI");

        // When
        Class<?> tipoRetorno = metodo.getReturnType();

        // Then
        assertEquals(OpenAPI.class, tipoRetorno);
    }

    @Test
    void deveVerificarSeMetodoEPublico() throws NoSuchMethodException {
        // Given
        Method metodo = OpenApiConfig.class.getMethod("customOpenAPI");

        // When
        boolean ePublico = java.lang.reflect.Modifier.isPublic(metodo.getModifiers());

        // Then
        assertTrue(ePublico);
    }

    @Test
    void deveValidarEstruturaDaDescricao() {
        // When
        OpenAPI resultado = openApiConfig.customOpenAPI();
        String descricao = resultado.getInfo().getDescription();

        // Then
        assertNotNull(descricao);
        assertFalse(descricao.trim().isEmpty());
        assertTrue(descricao.contains("pagamentos"));
        assertTrue(descricao.contains("pedidos"));
        assertTrue(descricao.contains("Microsserviço"));
    }

    @Test
    void deveGarantirQueOpenAPIEInstanciavalida() {
        // When
        OpenAPI resultado = openApiConfig.customOpenAPI();

        // Then
        assertNotNull(resultado);
        assertNotNull(resultado.getInfo());

        OpenAPI segundaInstancia = openApiConfig.customOpenAPI();
        assertNotSame(resultado, segundaInstancia);
    }

    @Test
    void deveVerificarTodasAsPropriedadesDoInfo() {
        // When
        OpenAPI resultado = openApiConfig.customOpenAPI();
        Info info = resultado.getInfo();

        // Then
        assertAll("Verificar todas as propriedades do Info",
                () -> assertNotNull(info.getTitle()),
                () -> assertNotNull(info.getVersion()),
                () -> assertNotNull(info.getDescription()),
                () -> assertFalse(info.getTitle().trim().isEmpty()),
                () -> assertFalse(info.getVersion().trim().isEmpty()),
                () -> assertFalse(info.getDescription().trim().isEmpty())
        );
    }
}