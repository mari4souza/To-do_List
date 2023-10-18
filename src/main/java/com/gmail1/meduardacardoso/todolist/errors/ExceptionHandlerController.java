//aqui dentro estou criando a customização de erros

package com.gmail1.meduardacardoso.todolist.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice //anotação no spring boot q é usada para definir classes globais no momento de tratamento de excessões
public class ExceptionHandlerController {
    
    //caso o usuario tente cadastrar uma task com titulo maior q 50 caracteres
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMostSpecificCause().getMessage());
    }
}
