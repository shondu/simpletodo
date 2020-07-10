package com.jensen.simpletodo.controllers;

import com.jensen.simpletodo.data.Todo;
import com.jensen.simpletodo.service.TodoServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
public class DataController {

    private final TodoServiceImpl todoServiceImpl;

    public DataController(TodoServiceImpl todoServiceImpl) {
        this.todoServiceImpl = todoServiceImpl;
    }

    @GetMapping("/data/todos")
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<?> getTodoList() {
        List<Todo> todoList = todoServiceImpl.getAllTodos();
        return new ResponseEntity<>(todoList, HttpStatus.OK);
    }

    @PutMapping("/data/todos")
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<?> putTodoList(@RequestBody List<Todo> todoList){

        this.todoServiceImpl.updateTodoList(todoList);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
