package com.fiap.pagamento.entity;

import jakarta.persistence.*;
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
    @Column(name = "pagamento_id", nullable = false, updatable = false)
    private String pagamentoId;

    @Column(name = "pedido_id", nullable = false)
    private String pedidoId;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "metodo_pagamento", nullable = false)
    private String metodoPagamento;

    @Column(nullable = false)
    private String status;
}