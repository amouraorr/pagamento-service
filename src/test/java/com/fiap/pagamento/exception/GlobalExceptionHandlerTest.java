package com.fiap.pagamento.exception;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    private ListAppender<ILoggingEvent> listAppender;
    private Logger logger;

    @BeforeEach
    void configurarLogs() {
        logger = (Logger) LoggerFactory.getLogger(GlobalExceptionHandler.class);
        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
        logger.setLevel(Level.ALL);
    }

    @Test
    void deveRetornarErroInternoQuandoOcorrerExcecaoGenerica() {
        // Given
        Exception exception = new RuntimeException("Erro de teste");
        LocalDateTime antesDoTeste = LocalDateTime.now().minusSeconds(1);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAllExceptions(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno no servidor", body.get("message"));

        LocalDateTime timestamp = (LocalDateTime) body.get("timestamp");
        assertNotNull(timestamp);
        assertTrue(timestamp.isAfter(antesDoTeste));
        assertTrue(timestamp.isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void deveRegistrarLogDeErroQuandoOcorrerExcecao() {
        // Given
        RuntimeException exception = new RuntimeException("Erro crítico no sistema");

        // When
        globalExceptionHandler.handleAllExceptions(exception);

        // Then
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(1, logsList.size());

        ILoggingEvent logEvent = logsList.get(0);
        assertEquals(Level.ERROR, logEvent.getLevel());
        assertEquals("Erro inesperado: {}", logEvent.getMessage());
        assertEquals("Erro crítico no sistema", logEvent.getArgumentArray()[0]);

        assertNotNull(logEvent.getThrowableProxy());
        assertEquals("java.lang.RuntimeException", logEvent.getThrowableProxy().getClassName());
        assertEquals("Erro crítico no sistema", logEvent.getThrowableProxy().getMessage());
    }

    @Test
    void deveRetornarErroInternoParaIllegalArgumentException() {
        // Given
        IllegalArgumentException exception = new IllegalArgumentException("Argumento inválido");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAllExceptions(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno no servidor", body.get("message"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    void deveRetornarErroInternoParaNullPointerException() {
        // Given
        NullPointerException exception = new NullPointerException("Valor nulo encontrado");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAllExceptions(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno no servidor", body.get("message"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    void deveRetornarErroInternoParaExcecaoComMensagemNula() {
        // Given
        RuntimeException exception = new RuntimeException((String) null);

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAllExceptions(exception);

        // Then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());

        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals("Erro interno no servidor", body.get("message"));
        assertTrue(body.containsKey("timestamp"));
    }

    @Test
    void deveRegistrarLogComMensagemNulaQuandoExcecaoNaoTemMensagem() {
        // Given
        RuntimeException exception = new RuntimeException((String) null);

        // When
        globalExceptionHandler.handleAllExceptions(exception);

        // Then
        List<ILoggingEvent> logsList = listAppender.list;
        assertEquals(1, logsList.size());

        ILoggingEvent logEvent = logsList.get(0);
        assertEquals(Level.ERROR, logEvent.getLevel());
        assertNull(logEvent.getArgumentArray()[0]);
    }

    @Test
    void deveRetornarMapaComDoisCamposObrigatorios() {
        // Given
        Exception exception = new Exception("Teste de estrutura");

        // When
        ResponseEntity<Map<String, Object>> response = globalExceptionHandler.handleAllExceptions(exception);

        // Then
        Map<String, Object> body = response.getBody();
        assertNotNull(body);
        assertEquals(2, body.size());
        assertTrue(body.containsKey("timestamp"));
        assertTrue(body.containsKey("message"));
        assertInstanceOf(LocalDateTime.class, body.get("timestamp"));
        assertInstanceOf(String.class, body.get("message"));
    }

    @Test
    void deveRetornarMesmaMensagemParaDiferentesExcecoes() {
        // Given
        RuntimeException runtime = new RuntimeException("Runtime error");
        IllegalStateException illegal = new IllegalStateException("State error");

        // When
        ResponseEntity<Map<String, Object>> response1 = globalExceptionHandler.handleAllExceptions(runtime);
        ResponseEntity<Map<String, Object>> response2 = globalExceptionHandler.handleAllExceptions(illegal);

        // Then
        assertEquals(response1.getStatusCode(), response2.getStatusCode());
        assertEquals(response1.getBody().get("message"), response2.getBody().get("message"));
    }

    @Test
    void deveGerarTimestampsDiferentesParaChamadasSequenciais() throws InterruptedException {
        // Given
        Exception exception1 = new RuntimeException("Primeiro erro");
        Exception exception2 = new RuntimeException("Segundo erro");

        // When
        ResponseEntity<Map<String, Object>> response1 = globalExceptionHandler.handleAllExceptions(exception1);
        Thread.sleep(1);
        ResponseEntity<Map<String, Object>> response2 = globalExceptionHandler.handleAllExceptions(exception2);

        // Then
        LocalDateTime timestamp1 = (LocalDateTime) response1.getBody().get("timestamp");
        LocalDateTime timestamp2 = (LocalDateTime) response2.getBody().get("timestamp");

        assertTrue(timestamp2.isAfter(timestamp1) || timestamp2.isEqual(timestamp1));
    }
}