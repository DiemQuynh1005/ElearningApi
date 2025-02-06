package com.edu.project_edu.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Blog;
import com.edu.project_edu.repositories.BlogRepository;

@Service
public class BlogService {
  @Autowired
  BlogRepository _blogRepository;

  public List<Blog> getAllBlog() {
    return _blogRepository.findAll();
  }

  public Blog saveBlog(Blog blog) {
    return _blogRepository.save(blog);
  }

  public Blog updateBlog(Blog blog) {
    try {
      return _blogRepository.save(blog);
    } catch (Exception e) {
      e.getStackTrace();
    }
    return null;
  }

  public Blog detaiBlog(int id) {
    return _blogRepository.findById(id).orElse(null);
  }

  // public void deleteBlog(int id) {
  // _blogRepository.deleteById(id);
  // }
}
