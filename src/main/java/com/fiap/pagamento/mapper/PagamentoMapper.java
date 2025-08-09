package com.fiap.pagamento.mapper;

import com.fiap.pagamento.domain.Pagamento;
import com.fiap.pagamento.dto.request.PagamentoRequestDTO;
import com.fiap.pagamento.dto.response.PagamentoResponseDTO;
import com.fiap.pagamento.entity.PagamentoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PagamentoMapper {

    @Mapping(target = "pagamentoId", ignore = true)
    @Mapping(target = "status", ignore = true)
    Pagamento toPagamento(PagamentoRequestDTO dto);

    PagamentoResponseDTO toPagamentoResponseDTO(Pagamento pagamento);

    PagamentoEntity toEntity(Pagamento pagamento);

    Pagamento toDomain(PagamentoEntity entity);
}