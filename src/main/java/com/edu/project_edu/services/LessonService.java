package com.edu.project_edu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Lesson;
import com.edu.project_edu.repositories.LessonRepository;

@Service
public class LessonService {
  @Autowired
  LessonRepository _lessonRepository;

  public List<Lesson> getAllLessons() {
    return _lessonRepository.findAll();
  }

  public List<Lesson> getAllLessonsByCourseId(int courseId) {
    return _lessonRepository.findByCourseId(courseId);
  }

  public Lesson saveLesson(Lesson lesson) {
    return _lessonRepository.save(lesson);
  }

  public Lesson updateLesson(Lesson lesson) {
    try {
      return _lessonRepository.save(lesson);
    } catch (Exception e) {
      e.getStackTrace();
    }
    return null;
  }

  public Lesson detailLesson(int id) {
    return _lessonRepository.findById(id).orElse(null);
  }

  public void deleteLesson(int id) {
    _lessonRepository.deleteById(id);
  }
}
