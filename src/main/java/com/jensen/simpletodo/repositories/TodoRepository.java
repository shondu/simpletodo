package com.jensen.simpletodo.repositories;

import com.jensen.simpletodo.data.Todo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TodoRepository extends CrudRepository<Todo, Long> {

    List<Todo> findAll();

}
