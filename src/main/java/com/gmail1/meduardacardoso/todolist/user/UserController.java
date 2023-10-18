package com.gmail1.meduardacardoso.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt; // criptografa senhas antes de mandar para o banco de dados

/*
 * Modificadores:
 * public
 * private
 * protected
 */
@RestController
@RequestMapping("/users")
public class UserController {

    /*
     * String
     * Integer
     * Double
     * Float
     * Char
     * Date
     * Void (metodo sem retorno)
     */

    // o RequestBody serve para dizer q as infos do UserModel vao vir dentro do Body
    // da Requisição

    @Autowired // instancia automaticamente
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel userModel) {
        var user = this.userRepository.findByUsername(userModel.getUsername());

        if (user != null) {
            // Mensagem de erro
            // Status Code
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe!");
        }

        // Bcrypt serve para criptografar a senha do usuario antes de guardar no banco
        // de dados
        var passwordHashred = BCrypt.withDefaults().hashToString(12, userModel.getPassword().toCharArray());

        userModel.setPassword(passwordHashred);

        var userCreated = this.userRepository.save(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userCreated);
    }
}
