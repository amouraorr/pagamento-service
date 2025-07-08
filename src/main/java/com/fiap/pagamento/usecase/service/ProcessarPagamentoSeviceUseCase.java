package com.fiap.pagamento.usecase.service;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.gateway.EstoqueServiceClient;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProcessarPagamentoSeviceUseCase {
    private final GatewayPagamentoPort gatewayPagamentoPort;
    private final EstoqueServiceClient estoqueServiceClient;

    public ProcessarPagamentoSeviceUseCase(GatewayPagamentoPort gatewayPagamentoPort, EstoqueServiceClient estoqueServiceClient) {
        this.gatewayPagamentoPort = gatewayPagamentoPort;
        this.estoqueServiceClient = estoqueServiceClient;
    }

    public Pagamento processar(Pagamento pagamento) {
        // Gerar ID e status do pagamento
        pagamento.setPagamentoId(UUID.randomUUID().toString());
        pagamento.setStatus("APROVADO");

        // Integração futura com gateway externo de pagamento
        // gatewayPagamentoPort.processarPagamentoExterno(pagamento);

        // Comunicação REST com estoque-service para reservar estoque
        try {
            estoqueServiceClient.reservarEstoque(pagamento.getPedidoId(), pagamento.getValor());
        } catch (Exception e) {
            // Em caso de erro, ajuste o status do pagamento
            pagamento.setStatus("FALHA_ESTOQUE");
            // Aqui pode-se lançar exceção ou tratar conforme a regra de negócio
        }

        // TODO: Adicionar integração com Kafka se necessário (producer/consumer)
        // Exemplo de comentário para futura integração:
        // kafkaTemplate.send("pagamento-processado", pagamento);

        return pagamento;
    }
}

