package com.fiap.pagamento.gateway;

import com.fiap.pagamento.domain.Pagamento;

public interface GatewayPagamentoPort {
    void processarPagamentoExterno(Pagamento pagamento);
}