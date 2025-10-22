package com.zario.projetos.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;

@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex) {
        String mensagemErro = "O recurso (Tarefa) solicitado não foi encontrado.";

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(mensagemErro);
    }
}
