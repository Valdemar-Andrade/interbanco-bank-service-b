package com.banco.banco2.services;

import java.util.List;

public interface TransacaoService<T, K> {

    public List<T> transacoes();
}
