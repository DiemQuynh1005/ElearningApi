package com.edu.project_edu.utils;

import org.springframework.stereotype.Component;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class MailUtil {

  @Value("${spring.mail.fromMail}")
  private String fromMail;

  @Autowired
  private JavaMailSender javaMailSender;

  @Autowired
  TemplateEngine templateEngine;

  /**
   * Send mail text
   * 
   * @param to
   * @param subject
   * @param body
   */
  public void sendEmail(String to, String subject, String body) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(to);
    message.setSubject(subject);
    message.setText(body);

    javaMailSender.send(message);
  }

  /**
   * Send mail with template
   *
   * @param to
   * @param subject
   * @param templateName
   * @param context
   */
  public void sendEmailWithHtmlTemplate(String to, String subject, String templateName, Context context) {
    MimeMessage mimeMessage = javaMailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

    try {
      helper.setTo(to);
      helper.setSubject(subject);
      String htmlContent = templateEngine.process(templateName, context);
      helper.setText(htmlContent, true);
      javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
      // Handle exception
    }
  }

  public void sendEmailWithAttachment(String to, String subject, String body, DataSource dataSource,
      String fileName) throws Exception {
    MimeMessage message = javaMailSender.createMimeMessage();

    MimeMultipart multipart = new MimeMultipart();

    // Phần text của email
    BodyPart textBodyPart = new MimeBodyPart();
    textBodyPart.setText(body);
    multipart.addBodyPart(textBodyPart);

    // Phần đính kèm (hình ảnh)
    MimeBodyPart attachmentPart = new MimeBodyPart();
    attachmentPart.setDataHandler(new DataHandler(dataSource));
    attachmentPart.setFileName(fileName);
    multipart.addBodyPart(attachmentPart);

    message.setSubject(subject);
    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
    message.setContent(multipart);

    // Gửi email
    javaMailSender.send(message);
  }
}
