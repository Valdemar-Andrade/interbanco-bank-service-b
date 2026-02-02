package com.banco.banco2.services.implementacao;

import com.banco.banco2.entities.Transacao;
import com.banco.banco2.repositories.TransacaoRepository;
import com.banco.banco2.services.TransacaoService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class TransacaoServiceServiceImpl extends AbstractService<Transacao, UUID> implements TransacaoService<Transacao, UUID> {

    @Override
    public List<Transacao> transacoes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Transacao editar(UUID pk_transacao, @NotNull Transacao transacao){
        transacao.setPk_transacao(pk_transacao);
        return super.editar(pk_transacao, transacao);
    }

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<Transacao> findByIbanOrigem(String iban_origem) {
        return transacaoRepository.findByIbanOrigem(iban_origem);
    }
}
