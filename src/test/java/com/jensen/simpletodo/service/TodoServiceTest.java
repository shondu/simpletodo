package com.jensen.simpletodo.service;

import com.jensen.simpletodo.data.Todo;
import com.jensen.simpletodo.repositories.TodoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TodoServiceTest {

    @Mock
    TodoRepository todoRepository;

    TodoService todoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        todoService = new TodoServiceImpl(todoRepository);
    }

    @Test
    void testGetAllTodos() {
        // given
        Todo todo1 = new Todo();
        todo1.setId(1L);
        Todo todo2 = new Todo();
        todo2.setId(2L);
        List<Todo> todoList = new ArrayList<>(Arrays.asList(todo1, todo2));

        when(todoRepository.findAll()).thenReturn(todoList);

        // when
        List<Todo> newTodoList = todoService.getAllTodos();

        // then
        assertEquals(2, newTodoList.size());
        verify(todoRepository, times(1)).findAll();
    }

    @Test
    void testUpdateTodoList() {
        // given
        Todo todo1 = new Todo();
        todo1.setId(1L);
        Todo todo2 = new Todo();
        todo2.setId(2L);
        List<Todo> todoList = new ArrayList<>(Arrays.asList(todo1, todo2));

        // when
        todoService.updateTodoList(todoList);

        // then
        verify(todoRepository, times(2)).findById(anyLong());
        verify(todoRepository, times(2)).save(any(Todo.class));
        verify(todoRepository, times(1)).findAll();
    }
}