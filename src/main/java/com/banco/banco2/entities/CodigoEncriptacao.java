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
@Table(name = "codigo_encriptacao")
public class CodigoEncriptacao {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "pk_codigo", updatable = false, nullable = false)
    private UUID pk_codigo;

    @Column(name = "codigo")
    private String codigo;


}
