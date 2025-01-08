package com.example.forum.exception;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MultipartException;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalExceptionHandler  {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Genel hata yakalayıcı
    @ExceptionHandler(Exception.class)
    public String handleUnexpectedError(Exception exception, Model model) {
        logger.error("Bir hata meydana geldi: ", exception);
        model.addAttribute("errorMessage", "Bir şeyler ters gitti. Lütfen tekrar deneyin.");
        return "error"; // Kullanıcıyı error.html'e yönlendir
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeError(RuntimeException runtimeException, Model model) {
        logger.error("Çalışma zamanı sırasında bir sorun oluştu: ", runtimeException);
        model.addAttribute("errorMessage", "Çalışma zamanı hatası oluştu: " + runtimeException.getMessage());
        return "error"; // Kullanıcıyı error.html'e yönlendir
    }

    @ExceptionHandler(MultipartException.class)
    public String handleFileUploadError(MultipartException multipartException, HttpSession session, Model model) {
        // Kullanıcının oturum kimliğini al
        Long currentUserId = (Long) session.getAttribute("loggedInUserId");
        
        if (currentUserId == null) {
            return "redirect:/login"; // Kullanıcı giriş yapmamışsa, giriş sayfasına yönlendir
        }

        logger.error("Dosya yükleme sırasında hata oluştu: {}", multipartException.getMessage());
        String customErrorMessage = "Yüklenen dosya çok büyük. Lütfen daha küçük boyutlu bir dosya seçin.";
        String encodedMessage = URLEncoder.encode(customErrorMessage, StandardCharsets.UTF_8);

        return "redirect:/profile/" + currentUserId + "?uploadError=" + encodedMessage; // Profil sayfasına yönlendir ve hata mesajını ekle
    }
}
