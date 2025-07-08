package com.fiap.pagamento.usecase.service;


import com.fiap.pagamento.domain.Pagamento;

public interface ProcessarPagamentoUseCase {
    Pagamento processar(Pagamento pagamento);
}