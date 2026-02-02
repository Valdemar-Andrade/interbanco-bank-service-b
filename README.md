# interbanco-bank-service-b

## Visão Geral
O **interbanco-bank-service-a** é um serviço backend responsável por simular uma instituição bancária participante do sistema **InterBanco**.  
Seu papel é processar solicitações de transferência recebidas via mensageria, validar regras de negócio e persistir as operações financeiras.

Este serviço faz parte de uma arquitetura distribuída orientada a eventos, utilizando **Apache Kafka** para comunicação assíncrona entre os componentes do sistema.

---

## Papel na Arquitetura
Neste ecossistema, o Bank Service A é responsável por:

- Representar um banco participante do sistema
- Consumir eventos de transferência provenientes do serviço intermediário
- Processar e validar operações financeiras
- Persistir dados relacionados às transferências
- Produzir eventos de resposta ou confirmação

---

## Tecnologias Utilizadas
- Java  
- Spring Boot  
- Apache Kafka  
- PostgreSQL  
- Maven  

---

## Fluxo de Funcionamento
1. O serviço intermediário publica um evento de solicitação de transferência no Kafka.
2. O **interbanco-bank-service-a** consome o evento.
3. As regras de negócio são aplicadas (validação e processamento).
4. A operação é persistida no banco de dados.
5. Um evento de confirmação é publicado para continuidade do fluxo.

---

## Como Executar Localmente

### Pré-requisitos
- Java 17+
- Maven
- Apache Kafka em execução
- PostgreSQL configurado

### Passos
```bash
git clone https://github.com/Valdemar-Andrade/interbanco-bank-service-b.git
cd interbanco-bank-service-b
mvn clean install
mvn spring-boot:run
```

## Persistência de Dados
O serviço utiliza PostgreSQL para armazenar informações relacionadas às transferências processadas, garantindo consistência e rastreabilidade das operações.

## Comunicação com Kafka
- Consumo de eventos de transferência
- Produção de eventos de resposta
- Processamento assíncrono para desacoplamento entre serviços

## Observações Técnicas
- O serviço foi desenvolvido com foco em separação de responsabilidades.
- A comunicação assíncrona via Kafka melhora a escalabilidade do sistema.
- Projeto criado com fins educacionais e de demonstração de arquitetura backend.

## Projetos Relacionados
- [interbanco-mediator-service](https://github.com/Valdemar-Andrade/interbanco-mediator-service) — Serviço intermediário responsável pela orquestração das transferências.
- [interbanco-bank-service-a](https://github.com/Valdemar-Andrade/interbanco-bank-service-a) — Serviço bancário participante do sistema InterBanco.
