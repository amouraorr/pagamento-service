package com.fiap.pagamento.gateway.impl;


import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import org.springframework.stereotype.Component;

@Component
public class GatewayPagamentoAdapter implements GatewayPagamentoPort {

    @Override
    public String processarPagamentoExterno(Pagamento pagamento) {
        // Simula processamento externo
        // Aqui você pode implementar lógica real ou simular aprovação/recusa
        // Exemplo simples:
        if (pagamento.getValor() > 1000) {
            return "RECUSADO";
        }
        return "APROVADO";
    }
}
