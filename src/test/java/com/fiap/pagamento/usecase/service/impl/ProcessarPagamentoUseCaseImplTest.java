package com.fiap.pagamento.usecase.service.impl;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.entity.PagamentoEntity;
import com.fiap.pagamento.gateway.EstoqueServiceClient;
import com.fiap.pagamento.gateway.GatewayPagamentoPort;
import com.fiap.pagamento.mapper.PagamentoMapper;
import com.fiap.pagamento.repository.PagamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessarPagamentoUseCaseImplTest {

    @Mock
    private GatewayPagamentoPort gatewayPagamentoPort;

    @Mock
    private EstoqueServiceClient estoqueServiceClient;

    @Mock
    private PagamentoRepository pagamentoRepository;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @InjectMocks
    private ProcessarPagamentoUseCaseImpl processarPagamentoUseCase;

    private Pagamento pagamento;
    private PagamentoEntity pagamentoEntity;
    private PagamentoEntity pagamentoEntitySalvo;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento();
        pagamento.setPedidoId("PED001");
        pagamento.setValor(500.0);
        pagamento.setMetodoPagamento("CARTAO_CREDITO");
        pagamento.setNumeroCartao("1234567890123456");

        pagamentoEntity = PagamentoEntity.builder()
                .pedidoId("PED001")
                .valor(500.0)
                .metodoPagamento("CARTAO_CREDITO")
                .numeroCartao("1234567890123456")
                .status("ABERTO")
                .build();

        pagamentoEntitySalvo = PagamentoEntity.builder()
                .pagamentoId("PAG001")
                .pedidoId("PED001")
                .valor(500.0)
                .metodoPagamento("CARTAO_CREDITO")
                .numeroCartao("1234567890123456")
                .status("ABERTO")
                .build();
    }

    @Test
    void deveProcessarPagamentoComSucessoCompleto() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class))).thenReturn("APROVADO");
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_COM_SUCESSO", pagamento.getStatus());
        assertEquals("PAG001", pagamento.getPagamentoId());

        verify(estoqueServiceClient).reservarEstoque("PED001", 500.0);
        verify(gatewayPagamentoPort).processarPagamentoExterno(pagamento);
        verify(pagamentoRepository, times(2)).save(any(PagamentoEntity.class));
        verify(estoqueServiceClient, never()).reporEstoque(anyString());
        assertNotNull(resultado);
    }

    @Test
    void deveProcessarPagamentoComPagamentoRecusado() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class))).thenReturn("RECUSADO");
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());
        doNothing().when(estoqueServiceClient).reporEstoque(anyString());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_SEM_CREDITO", pagamento.getStatus());
        assertEquals("PAG001", pagamento.getPagamentoId());

        verify(estoqueServiceClient).reservarEstoque("PED001", 500.0);
        verify(gatewayPagamentoPort).processarPagamentoExterno(pagamento);
        verify(estoqueServiceClient).reporEstoque("PED001");
        verify(pagamentoRepository, times(2)).save(any(PagamentoEntity.class));
        assertNotNull(resultado);
    }

    @Test
    void deveProcessarPagamentoComPagamentoRecusadoCaseInsensitive() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class))).thenReturn("recusado");
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());
        doNothing().when(estoqueServiceClient).reporEstoque(anyString());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_SEM_CREDITO", pagamento.getStatus());
        verify(estoqueServiceClient).reporEstoque("PED001");
        assertNotNull(resultado);
    }

    @Test
    void deveFalharQuandoReservaDeEstoqueFalhar() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class))).thenReturn(pagamentoEntitySalvo);
        doThrow(new RuntimeException("Estoque insuficiente"))
                .when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_SEM_ESTOQUE", pagamento.getStatus());
        assertEquals("PAG001", pagamento.getPagamentoId());

        verify(estoqueServiceClient).reservarEstoque("PED001", 500.0);
        verify(gatewayPagamentoPort, never()).processarPagamentoExterno(any(Pagamento.class));
        verify(pagamentoRepository, times(2)).save(any(PagamentoEntity.class));
        verify(estoqueServiceClient, never()).reporEstoque(anyString());
        assertNotNull(resultado);
    }

    @Test
    void deveProcessarPagamentoQuandoGatewayLancarExcecao() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class)))
                .thenThrow(new RuntimeException("Erro na comunicação com gateway"));
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());
        doNothing().when(estoqueServiceClient).reporEstoque(anyString());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_SEM_CREDITO", pagamento.getStatus());
        assertEquals("PAG001", pagamento.getPagamentoId());

        verify(estoqueServiceClient).reservarEstoque("PED001", 500.0);
        verify(gatewayPagamentoPort).processarPagamentoExterno(pagamento);
        verify(estoqueServiceClient).reporEstoque("PED001");
        verify(pagamentoRepository, times(2)).save(any(PagamentoEntity.class));
        assertNotNull(resultado);
    }

    @Test
    void deveProcessarPagamentoComFalhaAoReporEstoque() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class))).thenReturn("RECUSADO");
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());
        doThrow(new RuntimeException("Erro ao repor estoque"))
                .when(estoqueServiceClient).reporEstoque(anyString());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_SEM_CREDITO", pagamento.getStatus());
        assertEquals("PAG001", pagamento.getPagamentoId());

        verify(estoqueServiceClient).reservarEstoque("PED001", 500.0);
        verify(gatewayPagamentoPort).processarPagamentoExterno(pagamento);
        verify(estoqueServiceClient).reporEstoque("PED001");
        verify(pagamentoRepository, times(2)).save(any(PagamentoEntity.class));
        assertNotNull(resultado);
    }

    @Test
    void deveAtualizarPagamentoIdAposPrimeiroSalvamento() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class))).thenReturn(pagamentoEntitySalvo);
        doThrow(new RuntimeException("Estoque indisponível"))
                .when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());

        // When
        processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("PAG001", pagamento.getPagamentoId());
    }

    @Test
    void deveProcessarPagamentoComValorAlto() {
        // Given
        pagamento.setValor(2000.0);
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class))).thenReturn("APROVADO");
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_COM_SUCESSO", pagamento.getStatus());
        verify(estoqueServiceClient).reservarEstoque("PED001", 2000.0);
        verify(estoqueServiceClient, never()).reporEstoque(anyString());
        assertNotNull(resultado);
    }

    @Test
    void deveProcessarPagamentoComValorBaixo() {
        // Given
        pagamento.setValor(10.0);
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class))).thenReturn("APROVADO");
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());

        // When
        Pagamento resultado = processarPagamentoUseCase.processar(pagamento);

        // Then
        assertEquals("FECHADO_COM_SUCESSO", pagamento.getStatus());
        verify(estoqueServiceClient).reservarEstoque("PED001", 10.0);
        verify(estoqueServiceClient, never()).reporEstoque(anyString());
        assertNotNull(resultado);
    }

    @Test
    void deveManterPagamentoIdAoSalvarStatusFinal() {
        // Given
        when(pagamentoMapper.toEntity(any(Pagamento.class))).thenReturn(pagamentoEntity);
        when(pagamentoRepository.save(any(PagamentoEntity.class)))
                .thenReturn(pagamentoEntitySalvo)
                .thenReturn(pagamentoEntitySalvo);
        when(gatewayPagamentoPort.processarPagamentoExterno(any(Pagamento.class))).thenReturn("APROVADO");
        when(pagamentoMapper.toDomain(any(PagamentoEntity.class))).thenReturn(pagamento);
        doNothing().when(estoqueServiceClient).reservarEstoque(anyString(), anyDouble());

        // When
        processarPagamentoUseCase.processar(pagamento);

        // Then
        verify(pagamentoRepository, times(2)).save(argThat(entity ->
                "PAG001".equals(entity.getPagamentoId())
        ));
    }
}