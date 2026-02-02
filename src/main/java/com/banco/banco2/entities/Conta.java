package com.banco.banco2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;


@Getter
@Setter
@ToString
@Entity
@Table(name = "conta")
public class Conta {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "pk_conta", updatable = false, nullable = false)
    private UUID pk_conta;

    @Column(name = "iban")
    private String iban;

    @Column(name = "saldo_disponivel")
    private BigDecimal saldo_disponivel;

    @Column(name = "saldo_contabilistico")
    private BigDecimal saldo_contabilistico;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_cliente", referencedColumnName = "pk_cliente")
    private Cliente fk_cliente;

}
