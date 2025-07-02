package com.fiap.cliente.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Pagamento {
    private String pagamentoId;
    private String pedidoId;
    private Double valor;
    private String metodoPagamento;
    private String status;
}