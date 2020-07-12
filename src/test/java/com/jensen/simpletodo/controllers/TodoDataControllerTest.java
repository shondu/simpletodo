package com.jensen.simpletodo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jensen.simpletodo.data.Todo;
import com.jensen.simpletodo.service.TodoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TodoDataControllerTest {

    @Mock
    TodoService todoService;

    private ObjectMapper objectMapper;

    TodoDataController todoDataController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        todoDataController = new TodoDataController(todoService);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(todoDataController).build();
    }

    @Test
    void testGetTodoList() throws Exception{
        // given
        Todo todo1 = new Todo();
        todo1.setId(1L);
        Todo todo2 = new Todo();
        todo2.setId(2L);
        List<Todo> todoList = new ArrayList<>(Arrays.asList(todo1, todo2));
        String jsonString = objectMapper.writeValueAsString(todoList);

        when(todoService.getAllTodos()).thenReturn(todoList);

        // then
        mockMvc.perform(get(TodoDataController.DATA_TODOS_MAPPING))
                .andExpect(status().isOk())
                .andExpect(content().string(jsonString));
        verify(todoService, times(1)).getAllTodos();
    }

    @Test
    void testPutTodoList() throws Exception{
        // given
        Todo todo1 = new Todo();
        todo1.setId(1L);
        Todo todo2 = new Todo();
        todo2.setId(2L);
        List<Todo> todoList = new ArrayList<>(Arrays.asList(todo1, todo2));
        String jsonString = objectMapper.writeValueAsString(todoList);

        // then
        mockMvc.perform(put(TodoDataController.DATA_TODOS_MAPPING)
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().string(TodoDataController.SUCESS_STRING));

    }
}