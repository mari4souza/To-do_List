//toda requisição antes de ir de fato para a rota onde eu chamei, ela vai passar pelo filtro, por isso
// a validação do usuario, para ver se o user esta no banco de dados para q ele possa criar tasks

package com.gmail1.meduardacardoso.todolist.filter;

// usar ctrl .  no FilterTaskAuth para automatico
import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.gmail1.meduardacardoso.todolist.user.IUserRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;
//import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// se usa OncePerRequestFilter para q n tenha q transformar todos os servlet request e response em http
@Component
public class FilterTaskAuth extends OncePerRequestFilter {

    @Autowired
    private IUserRepository userRepository; // importando o repositorio de users pra validar usuario

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var servletPath = request.getServletPath();

        //se estiver em outras rotas q nao seja /tasks/, ja vai direto pro DoFilter
        if (servletPath.startsWith("/tasks/")) {

            // pegar a autenticação (usuario e senha)
            var authorization = request.getHeader("Authorization"); // Um header HTTP é uma parte da requisição ou
                                                                    // resposta HTTP que contém informações sobre a
                                                                    // requisição ou resposta.

            var authEncoded = authorization.substring("Basic".length()).trim(); // esta pegando o codigo do basic, o
                                                                                // trim remove espaço em branco

            byte[] authDecode = Base64.getDecoder().decode(authEncoded); // cria um array de bytes com o codigo do basic

            var authString = new String(authDecode);

            //System.out.println("Authorization");
            // System.out.println(authString); // vendo como ficou a string quase totalmente
            // decodificada

            String[] credencials = authString.split(":"); // gerar um array no formato ["mari4souza","123454"]
            String username = credencials[0];
            String password = credencials[1];

            //System.out.println(username);
            //System.out.println(password);

            // validar usuario (ver se ele existe no banco de dados)
            // vendo se o username existe no repositorio de usuarios
            var user = this.userRepository.findByUsername(username);
            if (user == null) {
                response.sendError(401);
                // caso o usuario seja diferente de nulo,ou seja, esta no banco de dados, ele
                // sera validado no else
            } else {
                // validar senha
                var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());// pegando a
                                                                                                          // senha q
                                                                                                          // decodifiquei,
                                                                                                          // tranbsformando
                                                                                                          // num array,
                // e comparando com o array de senhas do usuario
                if (passwordVerify.verified == true) {
                    request.setAttribute("idUser", user.getId()); // dps disso vou na controller task
                    filterChain.doFilter(request, response); // doFilter serve para Permitir que a requisição continue
                                                             // seu fluxo normal de processamento.
                } else {
                    response.sendError(401);
                }

            }
            // segue viagem
        } else {
            filterChain.doFilter(request, response);
        }
    }

}
