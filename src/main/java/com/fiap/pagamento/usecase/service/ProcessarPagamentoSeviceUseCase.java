package com.fiap.pagamento.usecase.service;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.gateway.EstoqueServiceClient;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class ProcessarPagamentoSeviceUseCase {

    private final GatewayPagamentoPort gatewayPagamentoPort;
    private final EstoqueServiceClient estoqueServiceClient;

    public ProcessarPagamentoSeviceUseCase(GatewayPagamentoPort gatewayPagamentoPort, EstoqueServiceClient estoqueServiceClient) {
        this.gatewayPagamentoPort = gatewayPagamentoPort;
        this.estoqueServiceClient = estoqueServiceClient;
    }

    public Pagamento processar(Pagamento pagamento) {
        pagamento.setPagamentoId(UUID.randomUUID().toString());
        pagamento.setStatus("APROVADO");

        log.info("Processando pagamento externo para pagamentoId={}", pagamento.getPagamentoId());
        // gatewayPagamentoPort.processarPagamentoExterno(pagamento);

        try {
            log.info("Reservando estoque para pedidoId={} e valor={}", pagamento.getPedidoId(), pagamento.getValor());
            estoqueServiceClient.reservarEstoque(pagamento.getPedidoId(), pagamento.getValor());
            log.info("Estoque reservado com sucesso para pedidoId={}", pagamento.getPedidoId());
        } catch (Exception e) {
            log.error("Erro ao reservar estoque para pedidoId={}: {}", pagamento.getPedidoId(), e.getMessage(), e);
            pagamento.setStatus("FALHA_ESTOQUE");
            // Opcional: lançar exceção customizada para ser tratada no controller advice
            // throw new EstoqueException("Falha ao reservar estoque", e);
        }

        return pagamento;
    }
}

