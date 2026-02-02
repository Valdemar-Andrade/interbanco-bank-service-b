package com.banco.banco2.controllers;

import com.banco.banco2.dto.TransacaoDTO;
import com.banco.banco2.entities.CodigoEncriptacao;
import com.banco.banco2.entities.Transacao;
import com.banco.banco2.services.implementacao.CodigoEncriptacaoServiceImpl;
import com.banco.banco2.services.implementacao.ContaServiceImpl;
import com.banco.banco2.utils.*;
import com.banco.banco2.services.implementacao.TransacaoServiceServiceImpl;
import com.banco.banco2.utils.ResponseBody;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/transacao")
public class TransacaoController extends BaseController{

    @Autowired
    private TransacaoServiceServiceImpl service;

    @Autowired
    private ContaServiceImpl serviceConta;

    private final ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private CodigoEncriptacaoServiceImpl serviceEncriptacao;

    @Value("${hostserver.name}")
    private String hostname;
    @Value("${hostserver.password}")
    private String password;
    @Value("${hostserver.port}")
    private String porta;

    @Autowired
    public TransacaoController(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<ResponseBody> listar(){
        return this.ok("Transacoes listadas com sucesso", this.service.findAll());
    }

    @PostMapping("/criar")
    public ResponseEntity<String> realizarTransacao(@RequestBody TransacaoDTO transacaoDTO){

        try {

            //validar o iban origem e destino se estao na formaracao correta
            if (ValidacaoIBAN.validarIBAN(transacaoDTO.getIbanOrigem()) && ValidacaoIBAN.validarIBAN(transacaoDTO.getIbanDestino()) && ValidacaoIBAN.verificarPrefixoIBAN(transacaoDTO.getIbanOrigem())){

                //Editar o saldo (Debito)
                //Validar se o debito foi realizado com sucesso
                if (serviceConta.debitar(transacaoDTO.getValor(), transacaoDTO.getIbanOrigem())){

                    //Adicionar a lista de transacoes feitas
                    transacaoDTO.setTransationalID(UUIDGenerator.gerarUUID());
                    ArrayTransacaoManipulator.arrayTransacoes.add(transacaoDTO);

                    //Preparar a transacao para ser enviada
                    transacaoDTO.setCaf_destino( ValidacaoIBAN.getCAF( transacaoDTO.getIbanDestino() ) );
                    transacaoDTO.setEstado( ValidacaoIBAN.getCAF(transacaoDTO.getIbanOrigem()) + "_intermediario" );

                    String transacaoJson = objectMapper.writeValueAsString(transacaoDTO);

                    //encriptar a mensagem antes de ser enviada
                    String chaveString = serviceEncriptacao.getChaveFromDB().getCodigo();
                    SecretKey secretKey = EncryptionUtil.stringToKey(chaveString);

                    String transacaoEncriptada = EncryptionUtil.encrypt(transacaoJson, secretKey);

                    //Enviar a transacao encriptada
                    kafkaTemplate.send("interban", transacaoEncriptada);

                    System.out.println("Transação de Banco BFA: " + transacaoDTO.getEstado());

                    //Salvar a operacao
                    Transacao transacaoSalvar = new Transacao();

                    transacaoSalvar.setValor(transacaoDTO.getValor());
                    transacaoSalvar.setIbanOrigem(transacaoDTO.getIbanOrigem());
                    transacaoSalvar.setIbanDestino(transacaoDTO.getIbanDestino());
                    transacaoSalvar.setEstado(transacaoDTO.getEstado());

                    service.criar(transacaoSalvar);

                    return new ResponseEntity<>(transacaoJson, HttpStatus.OK);
                }else {
                    System.out.println("Erro na operacao: valor invalido");
                    return new ResponseEntity<>("Erro na operacao: valor invalido", HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }else {
                System.out.println("Erro na operacao: formato do IBAN invalido");
                return new ResponseEntity<>("Erro na operacao: formato do IBAN invalido", HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>("Erro ao serializar TransacaoDTO para JSON.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/historico/{iban_origem}")
    public ResponseEntity<ResponseBody> consultarHistoricoCliente(@PathVariable String iban_origem){
        return this.ok("Historico Listado com sucesso", this.service.findByIbanOrigem(iban_origem));
    }

}
