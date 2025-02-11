package com.edu.project_edu.controllers;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.edu.project_edu.dto.AccountDTO;
import com.edu.project_edu.dto.LoginDTO;
// import com.edu.project_edu.dto.VerifTokenDTO;
import com.edu.project_edu.entities.Account;
import com.edu.project_edu.services.AccountService;
import com.edu.project_edu.services.VerificationService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
  @Value("${file.upload-dir}")
  private String uploadDir;

  @Autowired
  AccountService _accountService;

  @Autowired
  VerificationService _verificationService;

  /*
   * ----------------- SEND TOKEN VERIFICATE ACCOUNT -----------------
   */
  @PostMapping("verif_email") // http://localhost:8080/api/accounts/verif_email?email=abc@gmail.com
  public ResponseEntity<?> sendVerificationMail(@RequestParam @NotEmpty @Email String email) {
    boolean checkEmailExisted = _accountService.checkEmailExisted(email);
    if (checkEmailExisted) {
      return new ResponseEntity<>("Email already exists.", HttpStatus.CONFLICT); // 409 Conflict
    }
    boolean status = _verificationService.forRegisterAccount(email);
    if (status) {
      return new ResponseEntity<>(HttpStatus.OK); // Return status 200
    }
    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
  }

  // /*
  // * ----------------- VERIFICATE TOKEN -----------------
  // */
  // @PostMapping("verif_token")
  // public ResponseEntity<?> verificateRegisterToken(@Valid @RequestBody
  // VerifTokenDTO verifTokenDTO, BindingResult br) {
  // if (br.hasErrors()) {
  // return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
  // }
  // boolean status = _verificationService.verifyToken(verifTokenDTO.getToken(),
  // verifTokenDTO.getEmail());
  // if (status) {
  // return new ResponseEntity<>(HttpStatus.OK); // Return status 200
  // }
  // return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
  // }

  /*
   * ----------------- REGISTER NEW ACCOUNT -----------------
   */
  @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Account> create(@Valid AccountDTO accountDto, @RequestParam("avatar") MultipartFile file,
      BindingResult br) {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
    }
    try {
      boolean status = _verificationService.verifyToken(accountDto.getToken(), accountDto.getEmail());
      if (status) {
        Account saveAccount = new Account();
        BeanUtils.copyProperties(accountDto, saveAccount);
        if (!file.isEmpty()) {
          accountDto.setAvatar(file);
          Path path = Paths.get(uploadDir + "/accounts");
          if (!Files.exists(path)) {
            Files.createDirectories(path);
          }
          String fileName = new Timestamp(System.currentTimeMillis()).getTime() + "_" + file.getOriginalFilename();
          Path filePath = path.resolve(fileName);
          Files.copy(file.getInputStream(), filePath);
          saveAccount.setAvatar(fileName);
        }
        Account rs = _accountService.saveAccount(saveAccount);
        return new ResponseEntity<>(rs, HttpStatus.CREATED); // Return with status 201
      }
      return new ResponseEntity<>(HttpStatus.FORBIDDEN); // Return with status 403
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }

  /*
   * ----------------- LOGIN -----------------
   */
  @PostMapping("/login")
  public ResponseEntity<String> login(@RequestBody @Valid LoginDTO loginReq, BindingResult br)
      throws Exception {
    if (br.hasErrors()) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Return status 400
    }
    try {
      String resq = _accountService.login(loginReq);
      if (resq != null) {
        return ResponseEntity.ok(resq);
      }
      return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // Return status 401
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return status 500
    }
  }
}
