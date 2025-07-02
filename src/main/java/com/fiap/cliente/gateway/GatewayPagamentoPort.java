package com.fiap.cliente.gateway;

import com.fiap.cliente.domain.Pagamento;

public interface GatewayPagamentoPort {
    void processarPagamentoExterno(Pagamento pagamento);
}