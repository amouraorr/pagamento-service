package com.fiap.cliente.usecase;

import com.fiap.cliente.domain.Pagamento;
import com.fiap.cliente.gateway.GatewayPagamentoPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProcessarPagamentoUseCase {

    private final GatewayPagamentoPort gatewayPagamentoPort;

    public ProcessarPagamentoUseCase(GatewayPagamentoPort gatewayPagamentoPort) {
        this.gatewayPagamentoPort = gatewayPagamentoPort;
    }

    public Pagamento processar(Pagamento pagamento) {
        // Simulação de processamento de pagamento
        pagamento.setPagamentoId(UUID.randomUUID().toString());
        pagamento.setStatus("APROVADO");

        // Integração futura com gateway externo
        // gatewayPagamentoPort.processarPagamentoExterno(pagamento);

        return pagamento;
    }
}
