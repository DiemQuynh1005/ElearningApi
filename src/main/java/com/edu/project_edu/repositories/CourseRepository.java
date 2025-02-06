package com.edu.project_edu.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Course;

@Component
public interface CourseRepository extends JpaRepository<Course, Integer> {
  List<Course> findByIdIn(List<Integer> ids);

  Optional<Course> findByName(String name);
}
