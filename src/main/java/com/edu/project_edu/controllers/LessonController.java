package com.edu.project_edu.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edu.project_edu.dto.LessonDTO;
import com.edu.project_edu.entities.Course;
import com.edu.project_edu.entities.Lesson;
import com.edu.project_edu.services.AwsS3OperationService;
import com.edu.project_edu.services.CourseService;
import com.edu.project_edu.services.LessonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {
  @Value("${file.upload-dir}")
  private String uploadDir;

  @Autowired
  LessonService _lessonService;

  @Autowired
  CourseService _courseService;

  @Autowired
  AwsS3OperationService _awsS3Service;

  /*
   * ----------------- GET ALL LESSONS -----------------
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping()
  public List<Lesson> getAllLessons() {
    List<Lesson> list = _lessonService.getAllLessons();
    return list;
  }

  /*
   * ----------------- GET ALL LESSONS BY COURSE ID-----------------
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping(path = "/by-course/{id}")
  public List<Lesson> getAllLessonsByCourseId(@PathVariable int id) {
    List<Lesson> list = _lessonService.getAllLessonsByCourseId(id);
    return list;
  }

  /*
   * ----------------- CREATE NEW LESSON -----------------
   */
  @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Lesson> create(@Valid LessonDTO lessonDto, @RequestParam("video") MultipartFile file,
      BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
    }
    try {
      Lesson saveLesson = new Lesson();
      // find Course by course_id
      Course course = _courseService.detaiCourse(lessonDto.getCourse_id());
      if (course != null) {
        if (!file.isEmpty()) {
          lessonDto.setVideo(file);
          // Path path = Paths.get(uploadDir + "/lessons");
          // if (!Files.exists(path)) {
          // Files.createDirectories(path);
          // }
          // String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "_" +
          // file.getOriginalFilename();
          // Path filePath = path.resolve(fileName);
          // Files.copy(file.getInputStream(), filePath);
          String videoUrl = _awsS3Service.saveFiletoAwsS3Bucket(lessonDto.getVideo());
          BeanUtils.copyProperties(lessonDto, saveLesson);
          saveLesson.setView(0);
          saveLesson.setCourse(course);
          saveLesson.setVideo(videoUrl);
          Lesson rs = _lessonService.saveLesson(saveLesson);
          return new ResponseEntity<>(rs, HttpStatus.CREATED); // Return created Blog with status 201
        }
        return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // Return status 422
      }
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }

  /*
   * ----------------- UPDATE LESSON -----------------
   */

  @PutMapping(path = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Lesson> update(@PathVariable int id, @Valid LessonDTO lessonDto,
      @RequestParam(name = "video", required = false) MultipartFile file, BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      Lesson existingLesson = _lessonService.detailLesson(id);
      if (existingLesson == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      /*
       * ? Thiếu setView()! nên giữ số view hiện có hay cho set lại
       */

      // Update fields excluding video
      BeanUtils.copyProperties(lessonDto, existingLesson, "video");
      // Handle image upload
      if (file != null && !file.isEmpty()) {
        // Delete the old file if it exists
        _awsS3Service.deleteFileFromAwsS3Bucket(existingLesson.getVideo());

        // Save the new file
        String newVideoUrl = _awsS3Service.saveFiletoAwsS3Bucket(file);
        existingLesson.setVideo(newVideoUrl);
      }
      // Set Course
      Course course = _courseService.detaiCourse(lessonDto.getCourse_id());
      if (course == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      existingLesson.setCourse(course);
      Lesson updatedLesson = _lessonService.updateLesson(existingLesson);
      return new ResponseEntity<>(updatedLesson, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * ----------------- DELETE LESSON -----------------
   */
  @DeleteMapping(path = "/delete/{id}")
  public ResponseEntity<HttpStatus> deleteLesson(@PathVariable int id) {
    Lesson existingLesson = _lessonService.detailLesson(id);
    if (existingLesson == null) {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    try {
      _awsS3Service.deleteFileFromAwsS3Bucket(existingLesson.getVideo());
      _lessonService.deleteLesson(existingLesson.getId());
      return new ResponseEntity<>(HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * ----------------- DETAIL LESSON -----------------
   */
  @GetMapping(path = "/detail/{id}")
  public ResponseEntity<Lesson> detailLesson(@PathVariable int id) {
    Lesson lesson = _lessonService.detailLesson(id);
    if (lesson != null) {
      return ResponseEntity.ok(lesson);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
