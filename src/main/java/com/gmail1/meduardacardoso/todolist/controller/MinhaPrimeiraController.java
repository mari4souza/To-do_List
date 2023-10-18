package com.gmail1.meduardacardoso.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeiraRota")
// localhost:8080/primeiraRota ---- toda vez q no broswer eu acessar essa
// url/primeiraRota ele vai entrar nessa controller

public class MinhaPrimeiraController {

    /*
     * Métodos de acesso do HTTP
     * GET - buscar dado/info
     * POST - add dado/info
     * PUT - alterar dado/info
     * DELETE - remover dado/info
     * PATCH - alterar somente um parte do dado/info
     */

    // Método (Funcionalidade) de uma classe

    @GetMapping("/primeiroMetodo")
    public String primeiraMensagem() {
        return "Funcionou!";
    }
}

// Essa pasta controller é so pra testar