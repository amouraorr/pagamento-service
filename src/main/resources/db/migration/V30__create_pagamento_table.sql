CREATE TABLE pagamentos (
                            pagamento_id UUID PRIMARY KEY,
                            pedido_id    UUID NOT NULL,
                            valor        DOUBLE PRECISION NOT NULL,
                            metodo_pagamento VARCHAR(255) NOT NULL,
                            status       VARCHAR(255) NOT NULL,
                            numero_cartao VARCHAR(255)
);