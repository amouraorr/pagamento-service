package com.fiap.cliente.controller;

import com.fiap.cliente.dto.request.PagamentoRequestDTO;
import com.fiap.cliente.dto.response.PagamentoResponseDTO;
import com.fiap.cliente.mapper.PagamentoMapper;
import com.fiap.cliente.usecase.ProcessarPagamentoUseCase;
import lombok.var;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final ProcessarPagamentoUseCase processarPagamentoUseCase;
    private final PagamentoMapper pagamentoMapper;

    public PagamentoController(ProcessarPagamentoUseCase processarPagamentoUseCase, PagamentoMapper pagamentoMapper) {
        this.processarPagamentoUseCase = processarPagamentoUseCase;
        this.pagamentoMapper = pagamentoMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PagamentoResponseDTO processarPagamento(@RequestBody PagamentoRequestDTO pagamentoRequestDTO) {
        var pagamento = pagamentoMapper.toPagamento(pagamentoRequestDTO);
        var pagamentoProcessado = processarPagamentoUseCase.processar(pagamento);
        return pagamentoMapper.toPagamentoResponseDTO(pagamentoProcessado);
    }
}