package com.example.forum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.forum.model.ForumPost;
import com.example.forum.repository.ForumPostRepository;

@Service
public class ForumPostService {

    private final ForumPostRepository forumPostRepository;

    @Autowired
    public ForumPostService(ForumPostRepository forumPostRepository) {
        this.forumPostRepository = forumPostRepository;
    }
    //Yeni forum post ekleme
    public ForumPost addForumPost(ForumPost forumPost) {
        if (forumPost.getCreatedAt() == null) {
            forumPost.setCreatedAt(new java.util.Date());
        }
        return forumPostRepository.save(forumPost);
    }
    //Sayfalama ile forum postlarını alma
    public Page<ForumPost> getPostsWithPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return forumPostRepository.findAllByOrderByCreatedAtDesc(pageable);
    }
}
