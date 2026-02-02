package com.banco.banco2.services.implementacao;

import com.banco.banco2.entities.CodigoEncriptacao;
import com.banco.banco2.repositories.CodigoEncriptacaoRepository;
import com.banco.banco2.services.CodigoEncriptacaoService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CodigoEncriptacaoServiceImpl extends AbstractService<CodigoEncriptacao, UUID> implements CodigoEncriptacaoService<CodigoEncriptacao, UUID> {

    @Autowired
    CodigoEncriptacaoRepository encriptacaoRepository;

    @Override
    public List<CodigoEncriptacao> codigos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CodigoEncriptacao editar(UUID pk_codigoEncriptacao, @NotNull CodigoEncriptacao codigoEncriptacao){
        codigoEncriptacao.setPk_codigo(pk_codigoEncriptacao);
        return super.editar(pk_codigoEncriptacao, codigoEncriptacao);
    }


    public CodigoEncriptacao getChaveFromDB(){

        return this.encriptacaoRepository.findAll().get(0);
    }

}
