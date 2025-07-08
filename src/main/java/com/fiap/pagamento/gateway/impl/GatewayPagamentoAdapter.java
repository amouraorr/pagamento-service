package com.fiap.pagamento.gateway.impl;


import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import org.springframework.stereotype.Component;

@Component
public class GatewayPagamentoAdapter implements GatewayPagamentoPort {

    @Override
    public void processarPagamentoExterno(Pagamento pagamento) {
        // Implementação para processamento de pagamento externo
    }
}