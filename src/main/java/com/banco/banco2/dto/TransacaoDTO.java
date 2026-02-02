package com.banco.banco2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TransacaoDTO {

    @JsonProperty("pk_transacao")
    private UUID pk_transacao;

    @JsonProperty("transationalID")
    private UUID transationalID;

    @JsonProperty("iban_origem")
    private String ibanOrigem;

    @JsonProperty("iban_destino")
    private String ibanDestino;

    @JsonProperty("valor")
    private BigDecimal valor;

    @JsonProperty("caf_destino")
    private String caf_destino; //Codigo do Agente Financeiro

    @JsonProperty("estado")
    private String estado;

    @JsonProperty("descricao")
    private String descricao;


}
