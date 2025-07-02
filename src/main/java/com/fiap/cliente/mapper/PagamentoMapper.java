package com.fiap.cliente.mapper;

import com.fiap.cliente.domain.Pagamento;
import com.fiap.cliente.dto.request.PagamentoRequestDTO;
import com.fiap.cliente.dto.response.PagamentoResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PagamentoMapper {
    PagamentoMapper INSTANCE = Mappers.getMapper(PagamentoMapper.class);

    @Mapping(target = "pagamentoId", ignore = true)
    @Mapping(target = "status", ignore = true)
    Pagamento toPagamento(PagamentoRequestDTO dto);

    PagamentoResponseDTO toPagamentoResponseDTO(Pagamento pagamento);
}