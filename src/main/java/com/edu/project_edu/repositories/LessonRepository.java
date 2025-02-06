package com.edu.project_edu.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.edu.project_edu.entities.Lesson;

@Component
public interface LessonRepository extends JpaRepository<Lesson, Integer> {
  List<Lesson> findByCourseId(int courseId);
}
