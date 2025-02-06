package com.edu.project_edu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Homework;

@Component
public interface HomeworkRepository extends JpaRepository<Homework, Integer> {
  List<Homework> findByLessonId(int lessonId);
}
