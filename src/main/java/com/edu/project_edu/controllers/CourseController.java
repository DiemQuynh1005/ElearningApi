package com.edu.project_edu.controllers;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edu.project_edu.dto.CourseDTO;
import com.edu.project_edu.entities.Account;
import com.edu.project_edu.entities.Course;
import com.edu.project_edu.services.AccountService;
import com.edu.project_edu.services.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
  @Value("${file.upload-dir}")
  private String uploadDir;

  @Autowired
  CourseService _courseService;

  @Autowired
  AccountService _accountService;

  /*
   * ----------------- GET ALL COURSES -----------------
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping()
  public List<Course> getAllCourses() {
    List<Course> list = _courseService.getAllCourses();
    return list;
  }

  /*
   * ----------------- CREATE NEW COURSE -----------------
   */
  @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Course> create(@Valid CourseDTO courseDto, @RequestParam("thumbnail") MultipartFile file,
      BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
    }

    try {
      Account teacher = _accountService.getAccountById(courseDto.getAccount_id());
      if (teacher == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Return status 404
      }
      if (teacher.getRole().equals("TEACHER") || teacher.getRole().equals("ADMIN")) {
        Course saveCourse = new Course();
        if (!file.isEmpty()) {
          courseDto.setThumbnail(file);
          Path path = Paths.get(uploadDir + "/courses");
          if (!Files.exists(path)) {
            Files.createDirectories(path);
          }
          String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "_" + file.getOriginalFilename();
          Path filePath = path.resolve(fileName);
          Files.copy(file.getInputStream(), filePath);
          BeanUtils.copyProperties(courseDto, saveCourse);
          saveCourse.setThumbnail(fileName);
          saveCourse.setAccount(teacher);
          Course rs = _courseService.saveCourse(saveCourse);
          return new ResponseEntity<>(rs, HttpStatus.CREATED); // Return created Blog with status 201
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // Return status 422
      }
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Return status 401
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }

  /*
   * ----------------- UPDATE COURSE -----------------
   */
  @PutMapping(path = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Course> update(@PathVariable int id, @Valid CourseDTO courseDto,
      @RequestParam(name = "thumbnail", required = false) MultipartFile file, BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      Course existingCourse = _courseService.detaiCourse(id);
      if (existingCourse == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      // Update fields excluding image
      BeanUtils.copyProperties(courseDto, existingCourse, "thumbnail");

      // Handle image upload
      if (file != null && !file.isEmpty()) {
        // Delete the old image file if it exists
        String oldImagePath = uploadDir + "/courses/" + existingCourse.getThumbnail();
        File oldImageFile = new File(oldImagePath);
        if (oldImageFile.exists()) {
          oldImageFile.delete();
        }

        // Save the new image
        String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + "/courses/" + fileName);
        Files.copy(file.getInputStream(), filePath);
        existingCourse.setThumbnail(fileName);
      }

      Course updatedCourse = _courseService.updateCourse(existingCourse);
      return new ResponseEntity<>(updatedCourse, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * ----------------- DETAIL COURSE BY ID -----------------
   */
  @GetMapping(path = "/detail/{id}")
  public ResponseEntity<Course> detailCourse(@PathVariable int id) {
    Course course = _courseService.detaiCourse(id);
    if (course != null) {
      return ResponseEntity.ok(course);
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  /*
   * ----------------- DETAIL COURSE BY NAME -----------------
   */
  @GetMapping(path = "/detail-by-name/{name}")
  public ResponseEntity<Course> detailCourseByName(@PathVariable String name) {
    Course course = _courseService.detaiCourseByName(name);
    if (course != null) {
      return ResponseEntity.ok(course);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
