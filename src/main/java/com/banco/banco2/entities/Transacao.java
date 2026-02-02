package com.banco.banco2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@ToString
@Entity
@Table(name = "transacao")
public class Transacao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "pk_transacao", updatable = false, nullable = false)
    private UUID pk_transacao;

    @Column(name = "iban_origem")
    private String ibanOrigem;

    @Column(name = "iban_destino")
    private String ibanDestino;

    @Column(name = "valor")
    private BigDecimal valor;

    @CreationTimestamp
    @Column(name = "data_transacao")
    private LocalDateTime data_transacao;

    @Column(name = "estado")
    private String estado;
}
