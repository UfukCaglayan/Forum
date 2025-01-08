package com.example.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.forum.model.Interest;

@Repository
public interface InterestRepository extends JpaRepository<Interest, Long> {
   
}
