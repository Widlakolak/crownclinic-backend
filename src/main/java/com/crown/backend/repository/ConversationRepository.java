package com.crown.backend.repository;

import com.crown.backend.domain.Conversation;
import com.crown.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    @Query("SELECT c FROM Conversation c JOIN c.participants p1 JOIN c.participants p2 WHERE p1 = :participant1 AND p2 = :participant2")
    Optional<Conversation> findByParticipants(@Param("participant1") User participant1, @Param("participant2") User participant2);
}