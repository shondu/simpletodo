package com.jensen.simpletodo.service;

import com.jensen.simpletodo.data.Todo;
import com.jensen.simpletodo.repositories.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;

    public TodoServiceImpl(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    public List<Todo> getAllTodos(){
        return todoRepository.findAll();
    }

    public void updateTodoList(List<Todo> todoList) {

        for(Todo workTodo : todoList){

            Todo todoObject = null;

            Optional<Todo> todoOptional = this.todoRepository.findById(workTodo.getId());
            if(todoOptional.isPresent()) {
                todoObject = todoOptional.get();
            } else {
                todoObject = new Todo();
                todoObject.setId(workTodo.getId());

            }

            todoObject.setTodoValue(workTodo.getTodoValue());
            todoObject.setCompleted(workTodo.getCompleted());
            this.todoRepository.save(todoObject);
        }

    }
}
