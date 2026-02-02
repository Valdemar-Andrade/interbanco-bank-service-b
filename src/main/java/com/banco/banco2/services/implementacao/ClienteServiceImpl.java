package com.banco.banco2.services.implementacao;

import com.banco.banco2.entities.Cliente;
import com.banco.banco2.services.ClienteService;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ClienteServiceImpl extends AbstractService<Cliente, UUID> implements ClienteService<Cliente, UUID> {

    @Override
    public List<Cliente> clientes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Cliente editar(UUID pk_cliente, @NotNull Cliente cliente){
        cliente.setPk_cliente(pk_cliente);
        return super.editar(pk_cliente, cliente);
    }

}
