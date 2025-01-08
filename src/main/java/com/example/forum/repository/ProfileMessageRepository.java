package com.example.forum.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.forum.model.Member;
import com.example.forum.model.ProfileMessage;
public interface ProfileMessageRepository extends JpaRepository<ProfileMessage, Long> {
    List<ProfileMessage> findByReceiver(Member receiver);
}
