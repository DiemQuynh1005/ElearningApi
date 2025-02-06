package com.edu.project_edu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Course;
import com.edu.project_edu.repositories.CourseRepository;

@Service
public class CourseService {
  @Autowired
  CourseRepository _courseRepository;

  public List<Course> getAllCourses() {
    return _courseRepository.findAll();
  }

  public Course saveCourse(Course course) {
    return _courseRepository.save(course);
  }

  public Course updateCourse(Course course) {
    try {
      return _courseRepository.save(course);
    } catch (Exception e) {
      e.getStackTrace();
    }
    return null;
  }

  public Course detaiCourse(int id) {
    return _courseRepository.findById(id).orElse(null);
  }

  public Course detaiCourseByName(String name) {
    return _courseRepository.findByName(name).orElse(null);
  }

  public List<Course> getCoursesByIds(List<Integer> ids) {
    return _courseRepository.findByIdIn(ids);
  }

}
