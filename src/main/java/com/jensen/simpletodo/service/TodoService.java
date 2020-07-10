package com.jensen.simpletodo.service;

import com.jensen.simpletodo.data.Todo;

import java.util.List;

public interface TodoService {

    List<Todo> getAllTodos();

    void updateTodoList(List<Todo> todoList);
}
