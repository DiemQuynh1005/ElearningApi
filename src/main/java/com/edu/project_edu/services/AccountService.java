package com.edu.project_edu.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.edu.project_edu.dto.LoginDTO;
import com.edu.project_edu.entities.Account;
import com.edu.project_edu.repositories.AccountRepository;
import com.edu.project_edu.utils.JwtUtil;

@Service
public class AccountService {
  @Autowired
  AccountRepository _accountRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  JwtUtil jwtUtil;

  public Account getAccountById(int id) {
    return _accountRepository.findById(id).orElse(null);
  }

  public boolean checkEmailExisted(String email) {
    return _accountRepository.findByEmail(email) != null;
  }

  public Account saveAccount(Account account) {

    try {
      account.setPassword(passwordEncoder.encode(account.getPassword()));
      _accountRepository.save(account);
      // ? mailRegisterUserComplete
      return account;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public String login(LoginDTO loginReq) throws Exception {
    try {
      Authentication authentication = authenticationManager
          .authenticate(
              new UsernamePasswordAuthenticationToken(loginReq.getEmail(),
                  loginReq.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authentication);

      String email = authentication.getName();
      Account user = _accountRepository.findByEmail(email);
      return jwtUtil.generateToken(user);
    } catch (Exception e) {
      throw new Exception("Email or password incorrect");
    }
  }
}
