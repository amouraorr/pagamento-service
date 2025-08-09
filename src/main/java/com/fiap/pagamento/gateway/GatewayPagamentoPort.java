package com.fiap.pagamento.gateway;

import com.fiap.pagamento.domain.Pagamento;

public interface GatewayPagamentoPort {
    String processarPagamentoExterno(Pagamento pagamento);
}