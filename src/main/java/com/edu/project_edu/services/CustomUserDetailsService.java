package com.edu.project_edu.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.edu.project_edu.entities.Account;
import com.edu.project_edu.repositories.AccountRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  AccountRepository _accountRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    Account user = _accountRepository.findByEmail(email);
    if (user != null) {
      List<GrantedAuthority> authorities = new ArrayList<>();
      if (user.getRole().equals("ADMIN")) {
        authorities.add(new SimpleGrantedAuthority("ADMIN"));
      } else if (user.getRole().equals("TEACHER")) {
        authorities.add(new SimpleGrantedAuthority("TEACHER"));
      }
      authorities.add(new SimpleGrantedAuthority("USER"));
      return new org.springframework.security.core.userdetails.User(
          user.getEmail(),
          user.getPassword(),
          authorities);
    } else {
      throw new UsernameNotFoundException("Invalid email or password.");
    }
  }
}
