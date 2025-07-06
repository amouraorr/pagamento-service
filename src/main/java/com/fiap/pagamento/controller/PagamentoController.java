package com.fiap.pagamento.controller;

import com.fiap.pagamento.dto.request.PagamentoRequestDTO;
import com.fiap.pagamento.dto.response.PagamentoResponseDTO;
import com.fiap.pagamento.mapper.PagamentoMapper;
import com.fiap.pagamento.usecase.service.ProcessarPagamentoSeviceUseCase;
import lombok.var;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamentos")
public class PagamentoController {

    private final ProcessarPagamentoSeviceUseCase processarPagamentoUseCase;
    private final PagamentoMapper pagamentoMapper;

    public PagamentoController(ProcessarPagamentoSeviceUseCase processarPagamentoUseCase, PagamentoMapper pagamentoMapper) {
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