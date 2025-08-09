package com.fiap.pagamento.gateway.impl;

import com.fiap.pagamento.domain.Pagamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class GatewayPagamentoAdapterTest {

    @InjectMocks
    private GatewayPagamentoAdapter gatewayPagamentoAdapter;

    private Pagamento pagamento;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento();
    }

    @Test
    void deveRecusarPagamentoQuandoNumeroCartaoForInvalido() {
        // Given
        pagamento.setNumeroCartao("0000000000000000");
        pagamento.setValor(500.0);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED001");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("RECUSADO", resultado);
    }

    @Test
    void deveRecusarPagamentoQuandoValorForMaiorQueMil() {
        // Given
        pagamento.setNumeroCartao("1234567890123456");
        pagamento.setValor(1500.0);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED002");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("RECUSADO", resultado);
    }

    @Test
    void deveRecusarPagamentoQuandoValorForExatamenteMaiorQueMil() {
        // Given
        pagamento.setNumeroCartao("1234567890123456");
        pagamento.setValor(1000.01);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED003");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("RECUSADO", resultado);
    }

    @Test
    void deveAprovarPagamentoQuandoValorForExatamenteMil() {
        // Given
        pagamento.setNumeroCartao("1234567890123456");
        pagamento.setValor(1000.0);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED004");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveAprovarPagamentoQuandoValorForMenorQueMil() {
        // Given
        pagamento.setNumeroCartao("1234567890123456");
        pagamento.setValor(999.99);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED005");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveAprovarPagamentoComValorBaixo() {
        // Given
        pagamento.setNumeroCartao("9876543210987654");
        pagamento.setValor(50.0);
        pagamento.setMetodoPagamento("CARTAO_DEBITO");
        pagamento.setPedidoId("PED006");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveRecusarPagamentoComCartaoInvalidoMesmoComValorBaixo() {
        // Given
        pagamento.setNumeroCartao("0000000000000000");
        pagamento.setValor(10.0);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED007");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("RECUSADO", resultado);
    }

    @Test
    void deveRecusarPagamentoComCartaoInvalidoEValorAlto() {
        // Given
        pagamento.setNumeroCartao("0000000000000000");
        pagamento.setValor(2000.0);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED008");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("RECUSADO", resultado);
    }

    @Test
    void deveAprovarPagamentoComValorZero() {
        // Given
        pagamento.setNumeroCartao("1111222233334444");
        pagamento.setValor(0.0);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setPedidoId("PED009");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveAprovarPagamentoComCartaoVazioMasValorValido() {
        // Given
        pagamento.setNumeroCartao("");
        pagamento.setValor(500.0);
        pagamento.setMetodoPagamento("PIX");
        pagamento.setPedidoId("PED010");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("APROVADO", resultado);
    }

    @Test
    void deveAprovarPagamentoComCartaoNuloMasValorValido() {
        // Given
        pagamento.setNumeroCartao(null);
        pagamento.setValor(750.0);
        pagamento.setMetodoPagamento("PIX");
        pagamento.setPedidoId("PED011");

        // When
        String resultado = gatewayPagamentoAdapter.processarPagamentoExterno(pagamento);

        // Then
        assertEquals("APROVADO", resultado);
    }
}