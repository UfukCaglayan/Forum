package com.example.forum.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.forum.model.Interest;
import com.example.forum.model.Member;
import com.example.forum.repository.InterestRepository;
import com.example.forum.repository.MemberRepository;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private InterestRepository interestRepository;

    // Tüm üyeleri listeleme
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Üyeye id'ye göre erişim
    public Member getMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Üye bulunamadı"));
    }

    // Üye güncelleme
    public void updateMember(Member member) {
        memberRepository.save(member);
    }

    // Üyeyi silme
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }

    // Üye oluşturma ve ilgi alanlarını ilişkilendirme
    public Member createMemberWithInterests(Member member, List<Long> interestIds) {
        // E-posta adresinin zaten kullanımda olup olmadığını kontrol et
        if (memberRepository.findByEposta(member.getEposta()).isPresent()) {
            throw new RuntimeException("Bu e-posta adresi zaten kullanımda.");
        }

        Set<Interest> interestsSet = new HashSet<>();
        for (Long interestId : interestIds) {
            Interest interest = interestRepository.findById(interestId)
                    .orElseThrow(() -> new RuntimeException("İlgi alanı bulunamadı"));
            interestsSet.add(interest);
        }

        member.setInterests(interestsSet);
        return memberRepository.save(member);
    }

    // E-posta adresiyle üyeyi alma
    public Member getMemberByEposta(String eposta) {
        return memberRepository.findByEposta(eposta)
                .orElseThrow(() -> new RuntimeException("Üye bulunamadı"));
    }

    // Profil fotoğrafını güncelleme
    public void updateProfileImg(Long memberId, String imageUrl) {
        Member member = getMemberById(memberId);
        if (member != null) {
            member.setProfileImg(imageUrl);
            updateMember(member);
        }
    }
}
