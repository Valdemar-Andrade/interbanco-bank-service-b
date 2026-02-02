package com.banco.banco2.repositories;

import com.banco.banco2.entities.CodigoEncriptacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CodigoEncriptacaoRepository extends JpaRepository<CodigoEncriptacao, UUID> {



}
