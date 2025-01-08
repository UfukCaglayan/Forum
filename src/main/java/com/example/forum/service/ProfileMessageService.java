package com.example.forum.service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.model.Member;
import com.example.forum.model.ProfileMessage;
import com.example.forum.repository.ProfileMessageRepository;

@Service
public class ProfileMessageService {

    @Autowired
    private ProfileMessageRepository profileMessageRepository;

    // Üyeye gelen tüm mesajları alma
    public List<ProfileMessage> getMessagesForMember(Member member) {
        return profileMessageRepository.findByReceiver(member);
    }

    // Profil mesajını kaydetme
    public void saveProfileMessage(ProfileMessage profileMessage) {
        profileMessageRepository.save(profileMessage);
    }

    // Verilen tarihin formatlanmış halini döndürme
    public String getFormattedSentAt(LocalDateTime sentAt) {
        if (sentAt != null) {
            Date sentAtDate = convertToSentAtDate(sentAt);
            return new SimpleDateFormat("dd MMM yyyy HH:mm").format(sentAtDate);
        }
        return "Tarih bilgisi yok"; 
    }

    private Date convertToSentAtDate(LocalDateTime localSentAtDate) {
        return Date.from(localSentAtDate.atZone(ZoneId.systemDefault()).toInstant());
    }
}
