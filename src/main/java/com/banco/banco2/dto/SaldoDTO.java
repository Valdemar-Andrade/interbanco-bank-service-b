package com.banco.banco2.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class SaldoDTO {

    @JsonProperty("transationalID")
    private UUID transationalID;

    @JsonProperty("pk_conta")
    private UUID pk_conta;

    @JsonProperty("caf")
    private String caf;

    @JsonProperty("estado")
    private String estado;

    //Dados da conta consultada
    @JsonProperty("iban")
    private String iban;

    @JsonProperty("saldo_disponivel")
    private BigDecimal saldoDisponivel;

    @JsonProperty("saldo_contabilistico")
    private BigDecimal saldoContabilistico;

    @JsonProperty("fk_cliente")
    private UUID fk_cliente;

}
