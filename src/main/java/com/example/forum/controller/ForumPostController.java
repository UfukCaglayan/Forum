package com.example.forum.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.forum.model.ForumPost;
import com.example.forum.model.Member;
import com.example.forum.service.ForumPostService;
import com.example.forum.service.MemberService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForumPostController {

    private final ForumPostService forumPostService;
    private final MemberService memberService;

    @Autowired
    public ForumPostController(ForumPostService forumPostService, MemberService memberService) {
        this.forumPostService = forumPostService;
        this.memberService = memberService;
    }

    @GetMapping("/forum")
    public String getForumPosts(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "5") int pageSize,
            Model model) {

        Page<ForumPost> forumPostsPage = forumPostService.getPostsWithPagination(pageNo, pageSize);

        model.addAttribute("forumPosts", forumPostsPage);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", forumPostsPage.getTotalPages());

        return "forum";
    }

    @PostMapping("/forum")
public String postForumPost(@RequestParam String post, HttpSession session, Model model) {
    System.out.println("Metoda girildi."); // İlk kontrol
    Long loggedInMemberId = (Long) session.getAttribute("loggedInMemberId");

    if (loggedInMemberId == null) {
        System.out.println("Oturum bilgisi bulunamadı.");
        model.addAttribute("errorMessage", "Mesaj göndermeden önce giriş yapmalısınız.");
        return "forum";
    }

    if (post == null || post.trim().isEmpty()) {
        System.out.println("Mesaj içeriği boş.");
        model.addAttribute("errorMessage", "Mesaj içeriği boş olamaz.");
        return "forum";
    }

    System.out.println("Mesaj kaydediliyor.");
    ForumPost forumPost = new ForumPost();
    forumPost.setPost(post);
    Member member = memberService.getMemberById(loggedInMemberId);
    forumPost.setMember(member);

    forumPostService.addForumPost(forumPost);
    System.out.println("Mesaj başarıyla kaydedildi.");

    return "redirect:/forum";
}

}