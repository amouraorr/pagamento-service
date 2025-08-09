package com.fiap.pagamento.gateway;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "estoque-service", url = "${estoque.service.url}")
public interface EstoqueServiceClient {

    @PostMapping("/api/estoque/reservar")
    void reservarEstoque(@RequestParam String pedidoId, @RequestParam Double valor);

    @PostMapping("/api/estoque/repor")
    void reporEstoque(@RequestParam String pedidoId);

    @PostMapping("/api/estoque/baixa")
    void baixarEstoque(@RequestParam("produtoId") String produtoId, @RequestParam("quantidade") Integer quantidade);


}