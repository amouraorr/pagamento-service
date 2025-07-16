# pagamento-service

# Serviço de Pagamento - Backend

## Introdução

Este microsserviço faz parte de um sistema modular de gerenciamento de pedidos, focado no processamento de pagamentos. 
Ele é responsável pela validação dos dados de pagamento, simulação de integrações com sistemas externos, controle do status das transações 
e atualização correta do status dos pedidos conforme o resultado do pagamento.

## Objetivo do Projeto

O objetivo principal deste microsserviço é fornecer uma API robusta para processar pagamentos de pedidos, garantindo a comunicação com o sistema 
de estoque e simulando a integração com gateways externos.

## Requisitos do Sistema

Para executar este microsserviço, você precisará dos seguintes requisitos:

- **Sistema Operacional**: Windows, macOS ou Linux  
- **Memória RAM**: Pelo menos 4 GB recomendados  
- **Espaço em Disco**: Pelo menos 500 MB de espaço livre  
- **Software**:  
    - Docker e Docker Compose  
    - Java JDK 11 ou superior  
    - Maven 3.6 ou superior  
    - PostgreSQL  
    - Git  

## Estrutura do Projeto

A estrutura do projeto está organizada da seguinte forma:
```plaintext
pagamento-service/
│
├── src/
│ └── main/
│   ├── java/
│   │ └── com.fiap.pagamento
│   │   ├── config/ : Configurações de segurança e Swagger.
│   │   ├── controller/ : Controladores REST para endpoints de pagamento.
│   │   ├── domain/ : Entidades de domínio do pagamento.
│   │   ├── dto/ : Objetos de transferência de dados (DTOs) para requisições e respostas.
│   │   ├── entity/ : Entidades JPA para persistência.
│   │   ├── exception/ : Tratamento global de exceções.
│   │   ├── gateway/ : Implementação da camada de acesso a dados e integração com outros microsserviços.
│   │   ├── mapper/ : Mapeamento entre entidades, domínios e DTOs.
│   │   ├── repository/ : Repositórios JPA.
│   │   ├── usecase/ : Serviços de caso de uso para regras de negócio.
│   │   └── PagamentoServiceApplication.java : Classe principal da aplicação.
│   └── resources/
│       └── application.properties : Configurações da aplicação.
├── pom.xml : Arquivo de configuração do Maven.
├── Dockerfile : Arquivo para construção da imagem Docker.
├── docker-compose.yml : Arquivo para orquestração de contêineres.
└── README.md : Documentação do projeto.
```

## Segurança

A segurança do microsserviço é configurada com Spring Security.

## Visão Geral do Projeto

Este microsserviço é desenvolvido com Spring Boot e segue uma arquitetura limpa simplificada, separando claramente as responsabilidades entre domínio, 
persistência, casos de uso e interface.

## Arquitetura

A arquitetura segue o padrão MVC e princípios da Arquitetura Limpa, com camadas bem definidas:

- **Domain**: Representa as entidades de negócio (Pagamento).
- **UseCase**: Serviços que implementam regras de negócio e casos de uso.
- **Gateway**: Interface e implementação para acesso a dados e integração com outros microsserviços (ex: estoque).
- **Controller**: Exposição dos endpoints REST para interação externa.
- **Mapper**: Conversão entre entidades JPA, domínios e DTOs.

## Princípios de Design e Padrões de Projeto

### Princípios de Design

1. **Single Responsibility Principle (SRP)**:
    - Cada classe tem uma única responsabilidade, como o serviço de processamento de pagamento ou o controlador REST.

2. **Open/Closed Principle (OCP)**:
    - As classes são abertas para extensão e fechadas para modificação, facilitando manutenção e evolução.

### Padrões de Projeto

1. **MVC (Model-View-Controller)**:
    - Controladores REST atuam como controladores.
    - Domínio representa o modelo.
    - Respostas JSON são a "view".

2. **Gateway Pattern**:
    - Abstrai o acesso a dados e integrações externas, facilitando troca da implementação sem impactar o domínio.

3. **Mapper Pattern**:
    - Facilita a conversão entre diferentes camadas do sistema.

## Interação entre as Partes do Sistema

1. **Cliente**: Envia requisições HTTP para processar pagamentos.
2. **Controller**: Recebe as requisições e delega para os casos de uso.
3. **UseCase**: Executa regras de negócio, validações e chama o gateway para persistência e integração.
4. **Gateway**: Interage com o banco de dados via JPA e integra com o microsserviço de estoque via REST.
5. **Banco de Dados**: Armazena os dados dos pagamentos.

## Tecnologias Utilizadas

- **Spring Boot**: Framework para desenvolvimento Java.
- **Spring Security**: Segurança da aplicação.
- **Spring Data JPA**: Persistência com banco relacional.
- **PostgreSQL**: Banco de dados relacional.
- **Feign Client**: Comunicação REST com outros microsserviços.
- **MapStruct**: Mapeamento entre objetos.
- **Lombok**: Redução de boilerplate.
- **Swagger (Springdoc OpenAPI)**: Documentação da API.
- **Docker e Docker Compose**: Containerização e orquestração.

## Pré-requisitos

Antes de executar o microsserviço, certifique-se de ter instalado:

- Docker e Docker Compose
- Java JDK 11 ou superior
- Maven 3.6 ou superior
- PostgreSQL rodando localmente ou via container

## Como Executar o Projeto

1. Clone o repositório:
   ```bash
   git clone <url-do-repositorio>
   ```  
2. Navegue até o diretório do microsserviço pagamento-service:
   ```bash
   cd pagamento-service
   ```  
3. Compile e empacote o projeto com Maven:
   ```bash
   mvn clean package -DskipTests
   ```  
4. Configure o banco de dados PostgreSQL conforme `application.properties`.
5. Execute a aplicação localmente:
   ```bash
   mvn spring-boot:run
   ```  
6. Ou utilize Docker Compose para subir o serviço e banco:
   ```bash
   docker compose up
   ```

## Endpoints Principais

- `POST /api/pagamentos` - Processar pagamento de um pedido

## Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do repositório.
2. Crie uma branch para sua feature (`git checkout -b feature/nome-da-feature`).
3. Faça commit das suas alterações (`git commit -m 'Descrição da feature'`).
4. Envie para o repositório remoto (`git push origin feature/nome-da-feature`).
5. Abra um Pull Request.

## Licença

Este projeto é privado ou não possui licença específica.

## Referências e Recursos

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [MapStruct Documentation](https://mapstruct.org/documentation/stable/reference/html/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Swagger OpenAPI Documentation](https://springdoc.org/)

## Conclusão

Este microsserviço de pagamento exemplifica a aplicação de arquitetura limpa e boas práticas no desenvolvimento de microsserviços,
garantindo modularidade, escalabilidade e facilidade de manutenção dentro do sistema de gerenciamento de pedidos.