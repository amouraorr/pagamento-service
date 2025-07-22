package com.fiap.pagamento.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagamentoEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "pagamento_id", updatable = false, nullable = false)
    private String pagamentoId;

    @Column(name = "pedido_id", nullable = false)
    private String pedidoId;

    @Column(nullable = false)
    private Double valor;

    @Column(name = "metodo_pagamento", nullable = false)
    private String metodoPagamento;

    @Column(nullable = false)
    private String status;

    @Column(name = "numero_cartao")
    private String numeroCartao;
}