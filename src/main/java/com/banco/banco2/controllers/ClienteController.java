package com.banco.banco2.controllers;

import com.banco.banco2.entities.Cliente;
import com.banco.banco2.utils.ResponseBody;
import com.banco.banco2.services.implementacao.ClienteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cliente")
public class ClienteController extends BaseController {

    @Autowired
    private ClienteServiceImpl service;

    @Value("${hostserver.name}")
    private String hostname;
    @Value("${hostserver.password}")
    private String password;
    @Value("${hostserver.port}")
    private String porta;

    @GetMapping
    public ResponseEntity<ResponseBody> listar(){
        return this.ok("Clientes listados com sucesso", this.service.findAll());
    }

    @PostMapping("/criar")
    public ResponseEntity<ResponseBody> criar(@RequestBody Cliente cliente){
        return this.created("Cliente criado com sucesso", this.service.criar(cliente));
    }

    @PostMapping("/editar/{pk_cliente}")
    public Cliente editar(@PathVariable UUID pk_cliente, @RequestBody Cliente cliente){
        return this.service.editar(pk_cliente, cliente);
    }

}
