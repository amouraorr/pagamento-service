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
        // Gerar ID e status do pagamento
        pagamento.setPagamentoId(UUID.randomUUID().toString());
        pagamento.setStatus("APROVADO");

        // Simula integração com gateway externo (mock)
        gatewayPagamentoPort.processarPagamentoExterno(pagamento);

        // Tenta reservar estoque via serviço externo
        try {
            estoqueServiceClient.reservarEstoque(pagamento.getPedidoId(), pagamento.getValor());
            log.info("Estoque reservado com sucesso para pedidoId={}", pagamento.getPedidoId());
        } catch (Exception e) {
            log.error("Erro ao reservar estoque para pedidoId={}: {}", pagamento.getPedidoId(), e.getMessage(), e);
            pagamento.setStatus("FALHA_ESTOQUE");
            // Aqui pode lançar exceção ou tratar conforme regra de negócio
        }

        // Persiste pagamento no banco local
        PagamentoEntity entity = pagamentoMapper.toEntity(pagamento);
        PagamentoEntity salvo = pagamentoRepository.save(entity);

        // Retorna domínio atualizado
        return pagamentoMapper.toDomain(salvo);
    }
}