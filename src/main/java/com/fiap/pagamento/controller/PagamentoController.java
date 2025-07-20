package com.fiap.pagamento.controller;

import com.fiap.pagamento.dto.request.PagamentoRequestDTO;
import com.fiap.pagamento.dto.response.PagamentoResponseDTO;
import com.fiap.pagamento.mapper.PagamentoMapper;
import com.fiap.pagamento.usecase.service.ProcessarPagamentoUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
        log.info("Iniciando processamento de pagamento para pedidoId={}", pagamentoRequestDTO.getPedidoId());
        var pagamento = pagamentoMapper.toPagamento(pagamentoRequestDTO);
        var pagamentoProcessado = processarPagamentoUseCase.processar(pagamento);
        log.info("Pagamento processado com status={} para pagamentoId={}", pagamentoProcessado.getStatus(), pagamentoProcessado.getPagamentoId());
        return pagamentoMapper.toPagamentoResponseDTO(pagamentoProcessado);
    }
}