package com.edu.project_edu.mail_templates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.edu.project_edu.utils.MailUtil;

@Component
public class MailRegisterProcess {
  @Autowired
  MailUtil mailUtil;

  // private final String subject = "Mail register successfully";

  public boolean sendVerifycationEmail(String email, String token) {
    String subject = "Account Verification";
    Context context = new Context();
    context.setVariable("verificationCode", token);
    mailUtil.sendEmailWithHtmlTemplate(email, subject, "verificationEmail", context);
    return true;
  }
}
