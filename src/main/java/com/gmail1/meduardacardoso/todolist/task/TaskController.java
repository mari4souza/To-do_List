package com.gmail1.meduardacardoso.todolist.task;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gmail1.meduardacardoso.todolist.utils.Utils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired // spring gerenciar instancia do repositorio
    private ITaskRepository taskRepository;

    @PostMapping("/") // toda vez q quiser criar uma tarefa vai ser no tasks/
    // pegando o atributo que vem do body
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) { //recuperando o atributo q coloquei no reqeust, q vem do FilterTaskAuth 
        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID)idUser);

        //validanbdo se a tarefa esta sendo criada numa data q ja passou
        var currentDate = LocalDateTime.now();
        if (currentDate.isAfter(taskModel.getStartAt()) || currentDate.isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio / data de término deve ser maior que a data atual");
        }
        //verificando se a data de inicio é depois da de termino
        if (taskModel.getStartAt().isAfter(taskModel.getEndAt())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data de termino");
        }

        var task = this.taskRepository.save(taskModel);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/")
    public List<TaskModel> list(HttpServletRequest request) {
        var idUser = request.getAttribute("idUser");
        var tasks = this.taskRepository.findByIdUser((UUID)idUser); // retornando a lista de tasks do usuario
        return tasks;
    }

    //http://localhost:8080/tasks/jtgoijrkghoj (id de task), isso é uma variavel do path
    @PutMapping("/{id}")
    //update é a funcionalidade q o usuario tem de atualizar as infos da tarefa
    //aq estou pegando taskmodel q vem do body, httpservlet request pois preciso q o usuario seja autenticado (pelo id)
    // e tbm path variable pois preciso do id da  tarefa que vou alterar
    public ResponseEntity update(@RequestBody TaskModel taskModel, HttpServletRequest request, @PathVariable UUID id) {

        var task = this.taskRepository.findById(id).orElse(null); //preciso disso para fazer a mescla no utils

        //verificando se a task existe
        if(task==null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada");
        }

        //vendo se o IdUser q ta tentando alterar a task é o mesmo que o id do dono da task
        var idUser = request.getAttribute("idUser");

        if (!task.getIdUser().equals(idUser)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usurário não tem permissão para alterar essa tarefa");
        }


        Utils.copyNonNullPropertys(taskModel, task);
        var taskUpdated = this.taskRepository.save(task);
        return ResponseEntity.ok().body(taskUpdated);
    }

    //verifica todos os atributos dentro de um objeto e faz validação daqueles q sao nulos e q nao sao, procura a task no banco de dados
    //depois faz uma mescla dela com o objeto q estamos recebendo da requisição

}
