package com.atividades.bowie.model.dao;

import com.atividades.bowie.model.Activity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityDAO extends CrudRepository<Activity, Long> {

    Optional<Activity> findByActivityNameIgnoreCase(String name);

    Optional<Activity> findByDescriptionIgnoreCase(String description);

    List<Activity> findActivityByCategoryIgnoreCase(String category);

    List<Activity> findAll();

}
