package com.fiap.pagamento.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pagamento {

        @Id
        private String pagamentoId;
        private String pedidoId;
        private Double valor;
        private String metodoPagamento;
        private String status;
}