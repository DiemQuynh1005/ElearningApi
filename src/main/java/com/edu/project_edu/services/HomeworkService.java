package com.edu.project_edu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Homework;
import com.edu.project_edu.repositories.HomeworkRepository;

@Service
public class HomeworkService {
  @Autowired
  HomeworkRepository _homeworkRepository;

  public HomeworkService(HomeworkRepository homeworkRepository) {
    this._homeworkRepository = homeworkRepository;
  }

  public List<Homework> getAllHomeworks() {
    return _homeworkRepository.findAll();
  }

  public List<Homework> getAllHomeworksByLessonId(int lessonId) {
    return _homeworkRepository.findByLessonId(lessonId);
  }

  public Homework saveHomework(Homework homework) {
    try {
      return _homeworkRepository.save(homework);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public Homework getHomeworkById(int id) {
    return _homeworkRepository.findById(id).orElse(null);
  }
}
