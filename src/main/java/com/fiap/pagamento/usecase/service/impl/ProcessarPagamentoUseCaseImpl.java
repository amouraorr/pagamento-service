package com.fiap.pagamento.usecase.service.impl;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.entity.PagamentoEntity;
import com.fiap.pagamento.mapper.PagamentoMapper;
import com.fiap.pagamento.repository.PagamentoRepository;
import com.fiap.pagamento.gateway.EstoqueServiceClient;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import com.fiap.pagamento.usecase.service.ProcessarPagamentoUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessarPagamentoUseCaseImpl implements ProcessarPagamentoUseCase {

    private final GatewayPagamentoPort gatewayPagamentoPort;
    private final EstoqueServiceClient estoqueServiceClient;
    private final PagamentoRepository pagamentoRepository;
    private final PagamentoMapper pagamentoMapper;

    @Override
    public Pagamento processar(Pagamento pagamento) {
        // 1. Define status inicial ABERTO e gera ID
        pagamento.setStatus("ABERTO");
        pagamento.setPagamentoId(UUID.randomUUID().toString());

        // Salva pagamento com status ABERTO
        PagamentoEntity entityAberto = pagamentoMapper.toEntity(pagamento);
        pagamentoRepository.save(entityAberto);
        log.info("Pagamento iniciado com status ABERTO para pedidoId={}", pagamento.getPedidoId());

        // 2. Tenta reservar estoque (usando valor do pagamento)
        try {
            estoqueServiceClient.reservarEstoque(pagamento.getPedidoId(), pagamento.getValor());
            log.info("Estoque reservado com sucesso para pedidoId={}, valor={}", pagamento.getPedidoId(), pagamento.getValor());
        } catch (Exception e) {
            log.error("Falha ao reservar estoque para pedidoId={}: {}", pagamento.getPedidoId(), e.getMessage(), e);
            pagamento.setStatus("FECHADO_SEM_ESTOQUE");
            pagamentoRepository.save(pagamentoMapper.toEntity(pagamento));
            return pagamento;
        }

        // 3. Processa pagamento externo
        String statusPagamento;
        try {
            statusPagamento = gatewayPagamentoPort.processarPagamentoExterno(pagamento);
        } catch (Exception e) {
            log.error("Erro ao processar pagamento externo para pedidoId={}: {}", pagamento.getPedidoId(), e.getMessage(), e);
            statusPagamento = "RECUSADO";
        }

        // 4. Atualiza status conforme resultado do pagamento
        if ("RECUSADO".equalsIgnoreCase(statusPagamento)) {
            log.warn("Pagamento recusado para pedidoId={}", pagamento.getPedidoId());
            // Repor estoque em caso de pagamento recusado
            try {
                estoqueServiceClient.reporEstoque(pagamento.getPedidoId());
                log.info("Estoque reposto para pedidoId={} após pagamento recusado", pagamento.getPedidoId());
            } catch (Exception e) {
                log.error("Erro ao repor estoque para pedidoId={}: {}", pagamento.getPedidoId(), e.getMessage(), e);
            }
            pagamento.setStatus("FECHADO_SEM_CREDITO");
        } else {
            pagamento.setStatus("FECHADO_COM_SUCESSO");
        }

        // 5. Salva pagamento com status final
        PagamentoEntity entityFinal = pagamentoMapper.toEntity(pagamento);
        PagamentoEntity salvo = pagamentoRepository.save(entityFinal);

        log.info("Pagamento processado com status={} para pedidoId={}", pagamento.getStatus(), pagamento.getPedidoId());

        return pagamentoMapper.toDomain(salvo);
    }
}