package com.fiap.pagamento.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pagamento {

        private String pagamentoId;
        private String pedidoId;
        private Double valor;
        private String metodoPagamento;
        private String status;
        private String numeroCartao;
}