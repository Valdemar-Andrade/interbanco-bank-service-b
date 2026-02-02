package com.banco.banco2.repositories;

import com.banco.banco2.entities.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    public List<Transacao> findByIbanOrigem(String iban_origem);

}
