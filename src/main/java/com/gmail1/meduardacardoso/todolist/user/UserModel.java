package com.gmail1.meduardacardoso.todolist.user;

import java.time.LocalDateTime;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.mapping.Column;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data; // lib q foi inerida no pom - dependencies
//import lombok.Getter;
//import lombok.Setter;

@Data // importa getters and setter automaticamente
// @Setter
@Entity(name = "tb_users") // criando tabela
// este UserModel e um objeto
public class UserModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id; // primarykey

    @Column(unique = true)
    private String username;
    // @Getter, para ser atribuido a apenas um atributo, deve ser solocado em cima
    // do mesmo, aqui so me da getter de name
    private String name;
    private String password;

    @CreationTimestamp
    private LocalDateTime createdAt;
    // getters and setters

}
