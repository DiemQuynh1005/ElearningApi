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

import com.edu.project_edu.dto.BlogDTO;
import com.edu.project_edu.entities.Blog;
import com.edu.project_edu.services.BlogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/blogs")
public class BlogController {
  @Value("${file.upload-dir}")
  private String uploadDir;

  @Autowired
  BlogService _blogService;

  /*
   * ----------------- GET ALL BLOGS-----------------
   */
  @ResponseStatus(HttpStatus.OK)
  @GetMapping()
  public List<Blog> getAllBlogs() {
    List<Blog> list = _blogService.getAllBlog();
    return list;
  }

  /*
   * ----------------- CREATE NEW BLOG -----------------
   */
  @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Blog> create(@Valid BlogDTO blogDto, @RequestParam("image") MultipartFile file,
      BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
    }

    try {
      Blog saveBlog = new Blog();
      if (!file.isEmpty()) {
        blogDto.setImage(file);
        Path path = Paths.get(uploadDir + "/blogs");
        if (!Files.exists(path)) {
          Files.createDirectories(path);
        }
        String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "_" + file.getOriginalFilename();
        Path filePath = path.resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        BeanUtils.copyProperties(blogDto, saveBlog);
        saveBlog.setImage(fileName);
        Blog rs = _blogService.saveBlog(saveBlog);
        return new ResponseEntity<>(rs, HttpStatus.CREATED); // Return created Blog with status 201
      }
      return new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY); // Return status 422
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }

  /*
   * ----------------- UPDATE BLOG -----------------
   */
  @PutMapping(path = "/edit/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Blog> update(@PathVariable int id, @Valid BlogDTO blogDto,
      @RequestParam(name = "image", required = false) MultipartFile file, BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    try {
      Blog existingBlog = _blogService.detaiBlog(id);
      if (existingBlog == null) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }

      // Update fields excluding image
      BeanUtils.copyProperties(blogDto, existingBlog, "image");

      // Handle image upload
      if (file != null && !file.isEmpty()) {
        // Delete the old image file if it exists
        String oldImagePath = uploadDir + "/blogs/" + existingBlog.getImage();
        File oldImageFile = new File(oldImagePath);
        if (oldImageFile.exists()) {
          oldImageFile.delete();
        }

        // Save the new image
        String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + "/blogs/" + fileName);
        Files.copy(file.getInputStream(), filePath);
        existingBlog.setImage(fileName);
      }

      Blog updatedBlog = _blogService.updateBlog(existingBlog);
      return new ResponseEntity<>(updatedBlog, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /*
   * ----------------- DETAIL BLOG -----------------
   */
  @GetMapping(path = "/detail/{id}")
  public ResponseEntity<Blog> detailBlog(@PathVariable int id) {
    Blog blog = _blogService.detaiBlog(id);
    if (blog != null) {
      return ResponseEntity.ok(blog);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
