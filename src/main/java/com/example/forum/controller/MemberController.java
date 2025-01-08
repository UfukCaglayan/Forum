package com.example.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.forum.model.Interest;
import com.example.forum.model.Member;
import com.example.forum.service.InterestService;
import com.example.forum.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private InterestService interestService;

    // Kullanıcı kayıt formunu gösterir ve ilgi alanlarını modelde sunar
    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        List<Interest> interests = interestService.getAllInterests();  // İlgi alanlarını al
        model.addAttribute("interests", interests);  
        return "signup"; 
    }

    // Kullanıcı kayıt işlemini gerçekleştirir ve ilgi alanlarını bağlar
    @PostMapping("/signup")
public String registerMember(@RequestParam String fullName,
                             @RequestParam String eposta,
                             @RequestParam(required = false) List<Long> interests, 
                             @RequestParam String location,
                             @RequestParam(required = false) String description,
                             @RequestParam String password,
                             @RequestParam String confirmPassword,
                             Model model,
                             HttpSession session) { 
    try {
        // Şifre doğrulaması
        if (!password.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "Şifreler uyuşmuyor.");
            return "signup";
        }

        // En az bir ilgi alanı seçildi mi kontrolü
        if (interests == null || interests.isEmpty()) {
            model.addAttribute("errorMessage", "En az bir ilgi alanı seçilmeli.");
            return "signup";  // Hata mesajı ile tekrar signup sayfasına yönlendir
        }

        // Yeni üye oluşturuluyor
        Member member = new Member();
        member.setFullName(fullName);
        member.setEposta(eposta);
        member.setLocation(location);
        member.setDescription(description);
        member.setPassword(password);

        // Üyeyi ve ilişkili ilgi alanlarını kaydediyoruz
        Member savedMember = memberService.createMemberWithInterests(member, interests);

        // Kullanıcıyı giriş yapmış olarak işaretleyip profil sayfasına yönlendiriyoruz
        session.setAttribute("loggedInMemberId", savedMember.getMemberId());

        return "redirect:/profile/" + savedMember.getMemberId();  // Profil sayfasına yönlendirme
    } catch (RuntimeException e) {
        // Hata mesajını modele ekliyoruz
        model.addAttribute("errorMessage", e.getMessage());
        return "signup";  // Hata durumunda signup sayfasına geri dönüyoruz
    }
}


    // Kullanıcı giriş formunu sunar
    @GetMapping("/signin")
    public String showSigninForm() {
        return "signin"; 
    }

    // Giriş işlemini gerçekleştirir ve kullanıcıyı doğrular
    @PostMapping("/signin")
    public String signinMember(@RequestParam String eposta,
                               @RequestParam String password,
                               HttpSession session,
                               Model model) {
        try {
            Member member = memberService.getMemberByEposta(eposta);

            if (member != null && member.getPassword().equals(password)) {
                session.setAttribute("loggedInMemberId", member.getMemberId());
                return "redirect:/profile/" + member.getMemberId(); 
            } else {
                model.addAttribute("errorMessage", "E-posta veya şifre hatalı.");
                return "signin";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Bir hata oluştu: " + e.getMessage());
            return "signin";
        }
    }

    // Tüm üyeleri listeleyerek görüntüler
    @GetMapping("/members")
    public String listMembers(Model model) {
        List<Member> members = memberService.getAllMembers();  
        model.addAttribute("members", members);  
        return "members";  
    }

    // Kullanıcı oturumunu kapatır
    @GetMapping("/signout")
    public String signout(HttpSession session) {
        session.invalidate();  
        return "redirect:/";   
    }
}
