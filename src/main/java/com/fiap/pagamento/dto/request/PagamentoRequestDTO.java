package com.fiap.pagamento.dto.request;

import lombok.Data;

@Data
public class PagamentoRequestDTO {

    private String pedidoId;
    private Double valor;
    private String metodoPagamento;
}