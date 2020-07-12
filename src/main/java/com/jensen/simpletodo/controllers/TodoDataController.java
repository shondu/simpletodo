package com.jensen.simpletodo.controllers;

import com.jensen.simpletodo.data.Todo;
import com.jensen.simpletodo.service.TodoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class TodoDataController {

    private final TodoService todoService;

    public static final String DATA_TODOS_MAPPING = "/data/todos";
    public static final String SUCESS_STRING = "Success";

    public TodoDataController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping(DATA_TODOS_MAPPING)
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<?> getTodoList() {
        List<Todo> todoList = todoService.getAllTodos();
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @PutMapping(DATA_TODOS_MAPPING)
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<?> putTodoList(@RequestBody List<Todo> todoList){

        this.todoService.updateTodoList(todoList);

        return new ResponseEntity<>(SUCESS_STRING, HttpStatus.OK);
    }
}
