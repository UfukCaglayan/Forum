package com.example.forum.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.forum.model.Interest;
import com.example.forum.model.Member;
import com.example.forum.model.ProfileMessage;
import com.example.forum.service.MemberService;
import com.example.forum.service.ProfileMessageService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/profile")
public class ProfileMessageController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProfileMessageService profileMessageService;

    // Profil görüntüleme işlemi
    @GetMapping("/{memberId}")
    public String displayProfilePage(@PathVariable Long memberId, Model model, HttpSession session) {
        Long loggedInMemberId = (Long) session.getAttribute("loggedInMemberId");

        // Oturum kontrolü
        model.addAttribute("isLoggedIn", loggedInMemberId != null);

        // Üye bilgilerini al
        Member member = memberService.getMemberById(memberId);
        if (member == null) {
            return "redirect:/";  // Üye bulunamadığında ana sayfaya yönlendir
        }

        // Üye bilgileri
        model.addAttribute("member", member);
        model.addAttribute("fullName", member.getFullName());

        // Profil mesajları
        List<ProfileMessage> profileMessages = profileMessageService.getMessagesForMember(member);
        for (ProfileMessage profileMessage : profileMessages) {
             String formattedTimestamp = profileMessageService.getFormattedSentAt(profileMessage.getSentAt());
             profileMessage.setFormattedSentAt(formattedTimestamp); //Yorum tarihlerinin formatlanması
        }
        model.addAttribute("profileMessages", profileMessages);

        // İlgi alanları
        Set<Interest> interestsSet = member.getInterests();
        List<Interest> interestsList = new ArrayList<>(interestsSet);
        model.addAttribute("interests", interestsList);

        // Profilin sahibi mi kontrolü
        boolean isOwnProfile = member.getMemberId().equals(loggedInMemberId);
        model.addAttribute("isOwnProfile", isOwnProfile);

        return "profile";  // profile.html sayfası
    }

    // Yeni profil mesajı ekleme işlemi
    @PostMapping("/{memberId}/add-message")
    public String addProfileMessage(@PathVariable Long memberId, @RequestParam String messageText, HttpSession session) {
        Long loggedInMemberId = (Long) session.getAttribute("loggedInMemberId");

        // Giriş yapılmamışsa yönlendir
        if (loggedInMemberId == null) {
            return "redirect:/signin";
        }

        // Mesaj gönderilecek üye
        Member receiver = memberService.getMemberById(memberId);

        // Mesajı gönderen üye
        Member sender = memberService.getMemberById(loggedInMemberId);

        // Kendi profiline mesaj gönderilemez
        if (sender.getMemberId().equals(receiver.getMemberId())) {
            return "redirect:/profile/" + memberId;
        }

        // Mesajın geçerli olup olmadığını kontrol et
        if (messageText != null && !messageText.trim().isEmpty()) {
            ProfileMessage profileMessage = new ProfileMessage();
            profileMessage.setMessageContent(messageText);
            profileMessage.setSentAt(LocalDateTime.now());  
            profileMessage.setSender(sender);
            profileMessage.setReceiver(receiver);

            // Mesajı kaydet
            profileMessageService.saveProfileMessage(profileMessage);
        }

        // Mesaj sonrası profil sayfasına yönlendir
        return "redirect:/profile/" + memberId;
    }

    // Profil resmini değiştirme işlemi
    @PostMapping("/change-profile-image")
    public String updateProfileImage(@RequestParam("profileImage") MultipartFile profileImg, HttpSession session) throws IOException {
        Long loggedInMemberId = (Long) session.getAttribute("loggedInMemberId");

        if (loggedInMemberId == null) {
            return "redirect:/register";
        }

        // Fotoğraf yükleme dizini
        String uploadDir = "src/main/resources/static/profileImages/";
        Files.createDirectories(Paths.get(uploadDir));

        // Unique oluşturulan dosya adı
        String uniqueImgFileName = UUID.randomUUID().toString() + "-" + profileImg.getOriginalFilename();

        Path filePath = Paths.get(uploadDir + uniqueImgFileName);
        Files.write(filePath, profileImg.getBytes());

        memberService.updateProfileImg(loggedInMemberId, uniqueImgFileName);

        return "redirect:/profile/" + loggedInMemberId;
    }
}
