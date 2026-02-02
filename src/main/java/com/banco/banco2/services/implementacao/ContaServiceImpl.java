package com.banco.banco2.services.implementacao;

import com.banco.banco2.dto.ContaDTO;
import com.banco.banco2.entities.Conta;
import com.banco.banco2.services.ContaService;
import org.antlr.v4.runtime.misc.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ContaServiceImpl extends AbstractService<Conta, UUID> implements ContaService<Conta, UUID> {

    @Override
    public List<Conta> contas() { throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public Conta editar(UUID pk_conta, @NotNull Conta conta){
        conta.setPk_conta(pk_conta);
        return super.editar(pk_conta, conta);
    }

    //Creditar o valor recebido para a conta do receptor
    public Boolean creditar(BigDecimal valor, String iban_destino){

        BigDecimal novoSaldo;

        //A conta (iban_destino) existe?
        List<Conta> contas = this.findAll();

        //
        for(Conta conta: contas){
            //Conta encontrada
            if (conta.getIban().equals(iban_destino)){

                //Calcular o novo saldo
                novoSaldo = conta.getSaldo_disponivel().add(valor);

                //Atualizar o saldo
                conta.setSaldo_disponivel(novoSaldo);
                conta.setSaldo_contabilistico(novoSaldo);

                //Editar o saldo da conta
                this.editar(conta.getPk_conta(), conta);

                return true;
            }
        }

        return false;
    }

    //Descontar o valor a ser transferido na conta do Emissor
    public Boolean debitar(BigDecimal valor, String iban_origen){

        BigDecimal novoSaldo;

        //A conta (iban_origem) existe?
        List<Conta> contas = this.findAll();

        //
        for(Conta conta: contas){

            //Conta encontrada
            if (conta.getIban().equals(iban_origen)){

                //Validar se o valor nao e negativo e nem zero, e validar se o valor nao e maior que o saldo disponivel
                if (valor.signum() > 0 && valor.compareTo(conta.getSaldo_disponivel()) <= 0){

                    //Calcular o novo saldo
                    novoSaldo = conta.getSaldo_disponivel().subtract(valor);

                    //Atualizar o saldo
                    conta.setSaldo_disponivel(novoSaldo);
                    conta.setSaldo_contabilistico(novoSaldo);

                    //Editar o saldo da conta
                    this.editar(conta.getPk_conta(), conta);

                    return true;
                }else return false;
            }
        }

        return false;
    }
}
