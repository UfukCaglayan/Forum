package com.example.forum.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "profile_messages")
public class ProfileMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileMessageId;

    private String messageContent; 
    private LocalDateTime sentAt; 

    @ManyToOne
    private Member sender;  

    @ManyToOne
    private Member receiver;  

   
    private String formattedSentAt; 

    // Getter ve setter metotları
    public Long getProfileMessageId() {
        return profileMessageId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public Member getSender() {
        return sender;
    }

    public Member getReceiver() {
        return receiver;
    }

    public String getFormattedSentAt() {
        return formattedSentAt;
    }

    public void setProfileMessageId(Long profileMessageId) {
        this.profileMessageId = profileMessageId;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    // sentAt değiştiğinde formattedSentAt'ı güncelle
    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
        this.formattedSentAt = formatSentAt(sentAt);
    }

    public void setSender(Member sender) {
        this.sender = sender;
    }

    public void setReceiver(Member receiver) {
        this.receiver = receiver;
    }

    public void setFormattedSentAt(String formattedSentAt) {
        this.formattedSentAt = formattedSentAt;
    }

    // Timestamp'ı biçimlendir
    private String formatSentAt(LocalDateTime sentAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return sentAt.format(formatter);
    }
}
