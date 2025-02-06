package com.edu.project_edu.services;

import java.util.List;

// import java.util.Collections;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

// import org.springframework.web.client.RestTemplate;
import com.edu.project_edu.entities.UserSubmission;
import com.edu.project_edu.repositories.UserSubmissionRepository;

@Service
public class UserSubmissionService {
  @Autowired
  UserSubmissionRepository _submissionRepository;

  // @Autowired
  // private RestTemplate restTemplate;

  public List<UserSubmission> getAllSubmissions() {
    return _submissionRepository.findAll();
  }

  public UserSubmission saveSubmission(UserSubmission submission) {
    try {
      return _submissionRepository.save(submission);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  // public Boolean checkSpellingByAI(String sentence) {
  // String url =
  // "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyB9Ckwlluxnx1ka93LXZditdOz1L7yMGs4";

  // org.springframework.http.HttpHeaders headers = new
  // org.springframework.http.HttpHeaders();
  // headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

  // String fullPrompt = "Chỉ trả lời 'true' nếu thấy nội dung của câu này đúng về
  // mặt ngữ pháp, 'false' nếu nội dung của câu này sai về mặt ngữ pháp. Không bao
  // gồm bất kỳ văn bản nào khác trong câu trả lời của bạn. Nội dung của câu cần
  // phân tích: "
  // + sentence;

  // Map<String, Object> requestMap = new HashMap<>();
  // requestMap.put("contents", Collections.singletonList(
  // Collections.singletonMap("parts", Collections.singletonList(
  // Collections.singletonMap("text", fullPrompt)))));

  // HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestMap,
  // headers);

  // try {
  // ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST,
  // entity, Map.class);
  // Map<String, Object> responseBody = response.getBody();

  // if (responseBody != null && responseBody.containsKey("candidates")) {
  // List<Map<String, Object>> candidates = (List<Map<String, Object>>)
  // responseBody
  // .get("candidates");
  // if (!candidates.isEmpty()) {
  // Map<String, Object> firstCandidate = candidates.get(0);
  // if (firstCandidate.containsKey("content")) {
  // Map<String, Object> contentMap = (Map<String, Object>) firstCandidate
  // .get("content");
  // if (contentMap.containsKey("parts")) {
  // List<Map<String, Object>> parts = (List<Map<String, Object>>) contentMap
  // .get("parts");
  // if (!parts.isEmpty()) {
  // String text = ((String) parts.get(0).get("text")).trim()
  // .toLowerCase();
  // if ("true".equals(text)) {
  // return true;
  // } else if ("false".equals(text)) {
  // return false;
  // }
  // }
  // }
  // }
  // }
  // }
  // } catch (Exception e) {
  // e.printStackTrace();
  // throw new RuntimeException("Failed to check spelling", e);
  // }

  // // If we reach here, either an error occurred or we got a "neutral" response
  // // Wait for a short time before retrying to avoid overwhelming the API
  // try {
  // Thread.sleep(1000);
  // } catch (InterruptedException ie) {
  // Thread.currentThread().interrupt();
  // return null; // Return null if the thread is interrupted
  // }
  // return null;
  // }
}
