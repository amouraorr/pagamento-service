package com.fiap.pagamento.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoEntity {

    @Id
    private String pagamentoId;
    private String pedidoId;
    private Double valor;
    private String metodoPagamento;
    private String status;
}