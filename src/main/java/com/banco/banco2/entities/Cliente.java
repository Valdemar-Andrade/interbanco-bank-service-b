package com.banco.banco2.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "pk_cliente", updatable = false, nullable = false)
    private UUID pk_cliente;

    @Column(name = "nome")
    private String nome;

    @Column(name = "num_bi")
    private String num_bi;

    @Column(name = "telefone")
    private String telefone;

    @Column(name = "nif")
    private String nif;

    // Construtores
    public Cliente() {
    }
}
