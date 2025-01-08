package com.example.forum.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.model.Interest;
import com.example.forum.repository.InterestRepository;

@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    // Tüm ilgi alanlarını veritabanından alma
    public List<Interest> getAllInterests() {
        return interestRepository.findAll();
    }

    // Yeni bir ilgi alanı kaydetme
    public Interest saveInterest(Interest interest) {
        return interestRepository.save(interest);
    }

    // ID'ye göre ilgi alanı bulma
    public Interest getInterestById(Long id) {
        return interestRepository.findById(id).orElse(null);
    }
}
