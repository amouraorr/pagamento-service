package com.fiap.pagamento.controller;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.dto.request.PagamentoRequestDTO;
import com.fiap.pagamento.dto.response.PagamentoResponseDTO;
import com.fiap.pagamento.mapper.PagamentoMapper;
import com.fiap.pagamento.usecase.service.ProcessarPagamentoUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoControllerTest {

    @Mock
    private ProcessarPagamentoUseCase processarPagamentoUseCase;

    @Mock
    private PagamentoMapper pagamentoMapper;

    @InjectMocks
    private PagamentoController pagamentoController;

    private PagamentoRequestDTO pagamentoRequestDTO;
    private Pagamento pagamento;
    private Pagamento pagamentoProcessado;
    private PagamentoResponseDTO pagamentoResponseDTO;

    @BeforeEach
    void setUp() {

        pagamentoRequestDTO = new PagamentoRequestDTO();
        pagamentoRequestDTO.setPedidoId("1");
        pagamentoRequestDTO.setValor(100.00);
        pagamentoRequestDTO.setMetodoPagamento("CARTAO_CREDITO");
        pagamentoRequestDTO.setNumeroCartao("1234567890123456");

        pagamento = new Pagamento(
                null,
                "1",
                100.00,
                "CARTAO_CREDITO",
                null,
                "1234567890123456"
        );

        pagamentoProcessado = new Pagamento(
                "PAG123",
                "1",
                100.00,
                "CARTAO_CREDITO",
                "APROVADO",
                "1234567890123456"
        );

        pagamentoResponseDTO = new PagamentoResponseDTO();
        pagamentoResponseDTO.setPagamentoId("PAG123");
        pagamentoResponseDTO.setStatus("APROVADO");
        pagamentoResponseDTO.setNumeroCartao("1234567890123456");
    }

    @Test
    void deveInicializarDependenciasCorretamente() {

        assertNotNull(pagamentoController);
        assertNotNull(processarPagamentoUseCase);
        assertNotNull(pagamentoMapper);
    }

    @Test
    void deveProcessarPagamentoComSucesso() {
        // Given
        when(pagamentoMapper.toPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(any(Pagamento.class))).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(any(Pagamento.class))).thenReturn(pagamentoResponseDTO);

        // When
        PagamentoResponseDTO resultado = pagamentoController.processarPagamento(pagamentoRequestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(pagamentoResponseDTO.getPagamentoId(), resultado.getPagamentoId());
        assertEquals(pagamentoResponseDTO.getStatus(), resultado.getStatus());
        assertEquals(pagamentoResponseDTO.getNumeroCartao(), resultado.getNumeroCartao());
    }

    @Test
    void deveChamarMapperParaConverterRequestEmPagamento() {
        // Given
        when(pagamentoMapper.toPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(any(Pagamento.class))).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(any(Pagamento.class))).thenReturn(pagamentoResponseDTO);

        // When
        pagamentoController.processarPagamento(pagamentoRequestDTO);

        // Then
        verify(pagamentoMapper, times(1)).toPagamento(pagamentoRequestDTO);
    }

    @Test
    void deveChamarUseCaseParaProcessarPagamento() {
        // Given
        when(pagamentoMapper.toPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(any(Pagamento.class))).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(any(Pagamento.class))).thenReturn(pagamentoResponseDTO);

        // When
        pagamentoController.processarPagamento(pagamentoRequestDTO);

        // Then
        verify(processarPagamentoUseCase, times(1)).processar(pagamento);
    }

    @Test
    void deveChamarMapperParaConverterPagamentoEmResponse() {
        // Given
        when(pagamentoMapper.toPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(any(Pagamento.class))).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(any(Pagamento.class))).thenReturn(pagamentoResponseDTO);

        // When
        pagamentoController.processarPagamento(pagamentoRequestDTO);

        // Then
        verify(pagamentoMapper, times(1)).toPagamentoResponseDTO(pagamentoProcessado);
    }

    @Test
    void deveExecutarChamadasNaOrdemCorreta() {
        // Given
        when(pagamentoMapper.toPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(any(Pagamento.class))).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(any(Pagamento.class))).thenReturn(pagamentoResponseDTO);

        // When
        pagamentoController.processarPagamento(pagamentoRequestDTO);

        // Then
        var inOrder = inOrder(pagamentoMapper, processarPagamentoUseCase);
        inOrder.verify(pagamentoMapper).toPagamento(pagamentoRequestDTO);
        inOrder.verify(processarPagamentoUseCase).processar(pagamento);
        inOrder.verify(pagamentoMapper).toPagamentoResponseDTO(pagamentoProcessado);
    }

    @Test
    void deveRetornarResponseDTOCorreto() {
        // Given
        when(pagamentoMapper.toPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(any(Pagamento.class))).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(any(Pagamento.class))).thenReturn(pagamentoResponseDTO);

        // When
        PagamentoResponseDTO resultado = pagamentoController.processarPagamento(pagamentoRequestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals(pagamentoResponseDTO, resultado);
    }

    @Test
    void deveConterAnotacaoRestController() {
        // Given / When / Then
        assertTrue(PagamentoController.class.isAnnotationPresent(RestController.class));
    }

    @Test
    void deveConterAnotacaoRequestMapping() {

        assertTrue(PagamentoController.class.isAnnotationPresent(RequestMapping.class));

        RequestMapping requestMapping = PagamentoController.class.getAnnotation(RequestMapping.class);
        assertEquals("/api/pagamentos", requestMapping.value()[0]);
    }

    @Test
    void deveConterAnotacoesNoMetodoProcessarPagamento() throws NoSuchMethodException {
        // Given
        Method metodo = PagamentoController.class.getMethod("processarPagamento", PagamentoRequestDTO.class);

        // When
        assertTrue(metodo.isAnnotationPresent(PostMapping.class));
        assertTrue(metodo.isAnnotationPresent(ResponseStatus.class));

        ResponseStatus responseStatus = metodo.getAnnotation(ResponseStatus.class);
        assertEquals(HttpStatus.CREATED, responseStatus.value());
    }

    @Test
    void deveProcessarPagamentoComDadosValidos() {

        PagamentoRequestDTO requestComOutrosDados = new PagamentoRequestDTO();
        requestComOutrosDados.setPedidoId("999");
        requestComOutrosDados.setValor(250.75);
        requestComOutrosDados.setMetodoPagamento("PIX");
        requestComOutrosDados.setNumeroCartao(null);

        Pagamento pagamentoOutro = new Pagamento(
                null,
                "999",
                250.75,
                "PIX",
                null,
                null
        );

        Pagamento pagamentoProcessadoOutro = new Pagamento(
                "PAG999",
                "999",
                250.75,
                "PIX",
                "APROVADO",
                null
        );

        PagamentoResponseDTO responseOutro = new PagamentoResponseDTO();
        responseOutro.setPagamentoId("PAG999");
        responseOutro.setStatus("APROVADO");
        responseOutro.setNumeroCartao(null);

        when(pagamentoMapper.toPagamento(requestComOutrosDados)).thenReturn(pagamentoOutro);
        when(processarPagamentoUseCase.processar(pagamentoOutro)).thenReturn(pagamentoProcessadoOutro);
        when(pagamentoMapper.toPagamentoResponseDTO(pagamentoProcessadoOutro)).thenReturn(responseOutro);

        // When
        PagamentoResponseDTO resultado = pagamentoController.processarPagamento(requestComOutrosDados);

        // Then
        assertNotNull(resultado);
        assertEquals("PAG999", resultado.getPagamentoId());
        assertEquals("APROVADO", resultado.getStatus());
        assertNull(resultado.getNumeroCartao());
    }

    @Test
    void deveProcessarPagamentoComCartaoCredito() {
        // Given
        when(pagamentoMapper.toPagamento(any(PagamentoRequestDTO.class))).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(any(Pagamento.class))).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(any(Pagamento.class))).thenReturn(pagamentoResponseDTO);

        // When
        PagamentoResponseDTO resultado = pagamentoController.processarPagamento(pagamentoRequestDTO);

        // Then
        assertNotNull(resultado);
        assertEquals("PAG123", resultado.getPagamentoId());
        assertEquals("APROVADO", resultado.getStatus());
        assertEquals("1234567890123456", resultado.getNumeroCartao());

        verify(pagamentoMapper).toPagamento(pagamentoRequestDTO);
        verify(processarPagamentoUseCase).processar(pagamento);
        verify(pagamentoMapper).toPagamentoResponseDTO(pagamentoProcessado);
    }

    @Test
    void deveManterDadosDoRequestOriginal() {
        // Given
        when(pagamentoMapper.toPagamento(pagamentoRequestDTO)).thenReturn(pagamento);
        when(processarPagamentoUseCase.processar(pagamento)).thenReturn(pagamentoProcessado);
        when(pagamentoMapper.toPagamentoResponseDTO(pagamentoProcessado)).thenReturn(pagamentoResponseDTO);

        // When
        pagamentoController.processarPagamento(pagamentoRequestDTO);

        verify(pagamentoMapper).toPagamento(argThat(request ->
                "1".equals(request.getPedidoId()) &&
                        100.00 == request.getValor() &&
                        "CARTAO_CREDITO".equals(request.getMetodoPagamento()) &&
                        "1234567890123456".equals(request.getNumeroCartao())
        ));
    }
}