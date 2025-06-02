package com.crown.backend.repository;

import com.crown.backend.domain.Conversation;
import com.crown.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Optional<Conversation> findByParticipant1AndParticipant2(User participant1, User participant2);
    Optional<Conversation> findByParticipant2AndParticipant1(User participant1, User participant2);
}

