package com.fiap.pagamento.gateway.impl;


import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import org.springframework.stereotype.Component;

@Component
public class GatewayPagamentoAdapter implements GatewayPagamentoPort {

    @Override
    public String processarPagamentoExterno(Pagamento pagamento) {
        // Simula recusa se o número do cartão for "0000000000000000"
        if ("0000000000000000".equals(pagamento.getNumeroCartao())) {
            return "RECUSADO";
        }
        // Simula recusa se o valor for maior que 2 (opcional, se quiser manter)
        if (pagamento.getValor() > 2) {
            return "RECUSADO";
        }
        return "APROVADO";
    }
}
