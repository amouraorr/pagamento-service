package com.fiap.pagamento.gateway.impl;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import org.springframework.stereotype.Component;

@Component
public class GatewayPagamentoAdapter implements GatewayPagamentoPort {

    @Override
    public String processarPagamentoExterno(Pagamento pagamento) {
        if ("0000000000000000".equals(pagamento.getNumeroCartao())) {
            return "RECUSADO";
        }
        if (pagamento.getValor() > 1000) {
            return "RECUSADO";
        }
        return "APROVADO";
    }
}