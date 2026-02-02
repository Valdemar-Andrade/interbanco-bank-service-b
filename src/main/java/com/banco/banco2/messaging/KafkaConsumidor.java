package com.banco.banco2.messaging;

import com.banco.banco2.controllers.ContaController;
import com.banco.banco2.dto.ContaDTO;
import com.banco.banco2.dto.SaldoDTO;
import com.banco.banco2.dto.TransacaoDTO;
import com.banco.banco2.entities.Conta;
import com.banco.banco2.entities.Transacao;
import com.banco.banco2.services.CodigoEncriptacaoService;
import com.banco.banco2.services.implementacao.CodigoEncriptacaoServiceImpl;
import com.banco.banco2.services.implementacao.ContaServiceImpl;
import com.banco.banco2.services.implementacao.TransacaoServiceServiceImpl;
import com.banco.banco2.utils.ArrayTransacaoManipulator;
import com.banco.banco2.utils.EncryptionUtil;
import com.banco.banco2.utils.ValidacaoIBAN;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class KafkaConsumidor {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper;

    @Autowired
    public KafkaConsumidor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Autowired
    private TransacaoServiceServiceImpl serviceTransacao;

    @Autowired
    CodigoEncriptacaoServiceImpl codigoEncriptacaoService;

    @Autowired
    private ContaServiceImpl service;

    @Autowired
    private ContaServiceImpl serviceConta;

    private boolean originouAqui = false;

    @KafkaListener(topics = "interban", groupId = "interban.group.banco2")
    public void consumidor(String mensagemJson) throws JsonProcessingException {

        String chaveString = codigoEncriptacaoService.getChaveFromDB().getCodigo();
        SecretKey secretKey = EncryptionUtil.stringToKey(chaveString);

        TransacaoDTO transacaoDTO;
        SaldoDTO saldoDTO;

        try{

            //Descriptografar a transacao recebida e deserializar para TransacaoDTO
            transacaoDTO = objectMapper.readValue(desencriptarTransacao(mensagemJson, secretKey), TransacaoDTO.class);

            //Descriptografar a transacaoSaldo recebida e deserializar para SaldoDTO
            saldoDTO = objectMapper.readValue(desencriptarTransacao(mensagemJson, secretKey), SaldoDTO.class);

            //A mensagem veio do intermediario para o Banco B (Eu)
            if (transacaoDTO.getEstado().equalsIgnoreCase("intermediario_0050")){

                //Verificar se existe
                for (TransacaoDTO t: ArrayTransacaoManipulator.arrayTransacoes){

                    //Foi enviado inicialmente de mim
                    if (t.getTransationalID().equals(transacaoDTO.getTransationalID())){

                        //Realizar o credito
                        service.creditar(transacaoDTO.getValor(), transacaoDTO.getIbanDestino());

                        //Atualizar para concluido, finalizando a transacao
                        t.setEstado("concluido");
                        transacaoDTO.setEstado("concluido");

                        //Salvar a operacao
                        Transacao transacaoSalvar = new Transacao();

                        transacaoSalvar.setValor(t.getValor());
                        transacaoSalvar.setIbanOrigem(t.getIbanOrigem());
                        transacaoSalvar.setIbanDestino(t.getIbanDestino());
                        transacaoSalvar.setEstado(t.getEstado());

                        serviceTransacao.criar(transacaoSalvar);

                        //Logica para aplicar para o caso de concluido
                        System.out.println("Transacao sucesso B para B - valor: " + t.getValor() + " - CAF dest: " + t.getCaf_destino());

                        String transacaoJson = objectMapper.writeValueAsString(transacaoDTO);
                        kafkaTemplate.send("interban", transacaoJson);

                        originouAqui = true;
                    }
                }

                //Foi enviado de outro banco
                if(!originouAqui){

                    //Realizo o deposito na conta destinada (Eu)
                    service.creditar(transacaoDTO.getValor(), transacaoDTO.getIbanDestino());
                    System.out.println("Transacao sucesso BAI para BFA(eu) - valor: " + transacaoDTO.getValor() + " - UID: " + transacaoDTO.getTransationalID());

                    //Confirmo o recebimento da transacao que partiu de outro banco
                    transacaoDTO.setEstado( ValidacaoIBAN.getCAF(transacaoDTO.getIbanDestino()) + "_concluido");

                    //Salvar a operacao
                    Transacao transacaoSalvar = new Transacao();

                    transacaoSalvar.setValor(transacaoDTO.getValor());
                    transacaoSalvar.setIbanOrigem(transacaoDTO.getIbanOrigem());
                    transacaoSalvar.setIbanDestino(transacaoDTO.getIbanDestino());
                    transacaoSalvar.setEstado(transacaoDTO.getEstado());

                    serviceTransacao.criar(transacaoSalvar);

                    //Envio a mensagem atualizada
                    String transacaoJson = objectMapper.writeValueAsString(transacaoDTO);
                    kafkaTemplate.send("interban", transacaoJson);
                }

            }

            //Erros
            if (transacaoDTO.getEstado().equalsIgnoreCase("intermediario_0050_erro")){

                for (TransacaoDTO t: ArrayTransacaoManipulator.arrayTransacoes){

                    //Foi enviado inicialmente de mim
                    if (t.getTransationalID().equals(transacaoDTO.getTransationalID())){

                        //Atualizar o saldo para o valor anterior
                        service.creditar(transacaoDTO.getValor(), transacaoDTO.getIbanOrigem());

                        //Atualizar para erro e termina a transacao
                        t.setEstado("erro");
                        transacaoDTO.setEstado("erro");

                        System.out.println("\n\nErro na Transacao (destino nao existe): IBAN origem: " + t.getIbanOrigem() + " - " + t.getValor() + " - IBAN destino: " + t.getIbanDestino());
                    }
                }
            }

            //intermediario_bfa_consulta
            if (saldoDTO != null && saldoDTO.getEstado().equalsIgnoreCase("intermediario_0050_consulta")){

                ContaDTO conta = ContaController.toDTOOne(service.findById(saldoDTO.getPk_conta()));

                saldoDTO.setEstado("0050_intermediario_consulta");
                saldoDTO.setIban(conta.getIban());
                saldoDTO.setSaldoDisponivel(conta.getSaldo_disponivel());
                saldoDTO.setSaldoContabilistico(conta.getSaldo_contabilistico());
                saldoDTO.setFk_cliente(conta.getFk_cliente());

                String transacaoJson = objectMapper.writeValueAsString(saldoDTO);

                //encriptar a mensagem antes de ser enviada
                String transacaoEncriptada = EncryptionUtil.encrypt(transacaoJson, secretKey);

                kafkaTemplate.send("interban", transacaoEncriptada);
            }

        }catch (IllegalArgumentException e){}
        catch (JsonMappingException e) {}
        catch (JsonProcessingException e) {}
        catch (Exception e) {}

    }

    private String desencriptarTransacao(String mensagemJson, SecretKey secretKey){

        byte[] mensagemCriptografadaBytes = Base64.getDecoder().decode(mensagemJson);
        String transacaoDesencriptada = null;

        try {
            transacaoDesencriptada = EncryptionUtil.decrypt2(mensagemCriptografadaBytes, secretKey);
        } catch (Exception e) {

        }

        return transacaoDesencriptada;
    }

}
