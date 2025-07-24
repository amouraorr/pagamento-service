package com.fiap.pagamento.gateway.impl;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import org.springframework.stereotype.Component;

@Component
public class GatewayPagamentoAdapter implements GatewayPagamentoPort {

    @Override
    public String processarPagamentoExterno(Pagamento pagamento) {
        // Recusa se o número do cartão for "0000000000000000"
        if ("0000000000000000".equals(pagamento.getNumeroCartao())) {
            return "RECUSADO";
        }
        // Aprova se valor <= 1000, recusa acima disso (ajuste conforme sua regra)
        if (pagamento.getValor() > 1000) {
            return "RECUSADO";
        }
        return "APROVADO";
    }
}