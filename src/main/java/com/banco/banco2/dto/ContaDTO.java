package com.banco.banco2.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class ContaDTO {

    private UUID pk_conta;
    private String iban;
    private BigDecimal saldo_disponivel;
    private BigDecimal saldo_contabilistico;
    private UUID fk_cliente;

}
